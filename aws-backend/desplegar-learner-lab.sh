#!/usr/bin/env bash
# =============================================================================
# Cruz del Sur - Trazabilidad de equipaje de bodega (Grupo 6)
# Despliegue en AWS Academy Learner Lab (usa el rol LabRole; no crea roles IAM).
#
# Crea (o reutiliza si ya existen) exactamente lo que usa la app:
#   - Tablas DynamoDB: cds-equipajes (id), cds-destinos (id), cds-usuarios (codigo)
#   - Lambdas Python 3.13: funEquipajes, funDestinos, funAuth
#   - APIs HTTP (etapa $default con despliegue automático):
#       apiEquipajes -> funEquipajes  (5 rutas /equipajes)
#       apiDestinos  -> funDestinos   (GET /destinos)
#       apiAuth      -> funAuth       (POST /auth/login)
#   - Datos iniciales: destinos y usuarios de prueba (contraseña CruzDelSur2026)
#
# Uso (en la terminal del laboratorio o en CloudShell, dentro de aws-backend):
#   echo "TU_CLAVE_SECRETA" > .token_secret     # opcional: si no existe se genera
#   bash desplegar-learner-lab.sh               # crea o actualiza todo
#   bash desplegar-learner-lab.sh --ejemplos    # además carga 4 equipajes de ejemplo
#
# Al final muestra las 3 URLs para NetworkModule.kt.
# =============================================================================
set -euo pipefail
cd "$(dirname "$0")"

REGION="${AWS_REGION:-${AWS_DEFAULT_REGION:-us-east-1}}"
export AWS_DEFAULT_REGION="$REGION" AWS_PAGER=""
CARGAR_EJEMPLOS="${1:-}"

paso() { printf '\n\033[1;34m==> %s\033[0m\n' "$*" >&2; }
ok()   { printf '    \033[32m✔\033[0m %s\n' "$*" >&2; }
vacio() { [ -z "$1" ] || [ "$1" = "None" ]; }

paso "Cuenta y rol"
CUENTA=$(aws sts get-caller-identity --query Account --output text)
ROL_ARN=$(aws iam get-role --role-name LabRole --query Role.Arn --output text 2>/dev/null || echo "arn:aws:iam::${CUENTA}:role/LabRole")
ok "Cuenta ${CUENTA} | región ${REGION}"
ok "Rol ${ROL_ARN}"

# Clave para firmar los tokens de sesión (la misma en funAuth y funEquipajes)
if [ ! -s .token_secret ]; then
  openssl rand -hex 32 > .token_secret
fi
SECRETO=$(tr -d '\n\r ' < .token_secret)

# ---------------------------------------------------------------- Tablas ----
crear_tabla() { # nombre clave
  if aws dynamodb describe-table --table-name "$1" >/dev/null 2>&1; then
    ok "Tabla $1 ya existe"
  else
    aws dynamodb create-table --table-name "$1" \
      --attribute-definitions "AttributeName=$2,AttributeType=S" \
      --key-schema "AttributeName=$2,KeyType=HASH" \
      --billing-mode PAY_PER_REQUEST >/dev/null
    aws dynamodb wait table-exists --table-name "$1"
    ok "Tabla $1 creada"
  fi
}
paso "Tablas DynamoDB"
crear_tabla cds-equipajes id
crear_tabla cds-destinos id
crear_tabla cds-usuarios codigo

# --------------------------------------------------------------- Lambdas ----
empaquetar() { # carpeta -> ruta del zip
  local zip_ruta="/tmp/$(basename "$1").zip"
  rm -f "$zip_ruta"
  if command -v zip >/dev/null 2>&1; then
    (cd "$1" && zip -q -r "$zip_ruta" lambda_function.py)
  else
    python3 -c "import zipfile,sys; z=zipfile.ZipFile(sys.argv[1],'w'); z.write(sys.argv[2]+'/lambda_function.py','lambda_function.py'); z.close()" "$zip_ruta" "$1"
  fi
  echo "$zip_ruta"
}

crear_lambda() { # nombre carpeta json_variables
  local zip_ruta; zip_ruta=$(empaquetar "$2")
  local entorno="{\"Variables\":$3}"
  if aws lambda get-function --function-name "$1" >/dev/null 2>&1; then
    aws lambda update-function-code --function-name "$1" --zip-file "fileb://$zip_ruta" >/dev/null
    aws lambda wait function-updated-v2 --function-name "$1"
    aws lambda update-function-configuration --function-name "$1" --environment "$entorno" \
      --runtime python3.13 --handler lambda_function.lambda_handler --timeout 10 --memory-size 256 >/dev/null
    aws lambda wait function-updated-v2 --function-name "$1"
    ok "Lambda $1 actualizada"
  else
    aws lambda create-function --function-name "$1" --runtime python3.13 --handler lambda_function.lambda_handler \
      --role "$ROL_ARN" --zip-file "fileb://$zip_ruta" --timeout 10 --memory-size 256 \
      --environment "$entorno" >/dev/null
    aws lambda wait function-active-v2 --function-name "$1"
    ok "Lambda $1 creada"
  fi
}
paso "Funciones Lambda (Python 3.13)"
crear_lambda funEquipajes lambdas/funEquipajes "{\"TABLA_EQUIPAJES\":\"cds-equipajes\",\"TOKEN_SECRET\":\"${SECRETO}\"}"
crear_lambda funDestinos  lambdas/funDestinos  "{\"TABLA_DESTINOS\":\"cds-destinos\"}"
crear_lambda funAuth      lambdas/funAuth      "{\"TABLA_USUARIOS\":\"cds-usuarios\",\"TOKEN_SECRET\":\"${SECRETO}\"}"

# ------------------------------------------------------------------ APIs ----
crear_api() { # nombre -> id (crea la API y su etapa $default si no existen)
  local id
  id=$(aws apigatewayv2 get-apis --query "Items[?Name=='$1'].ApiId | [0]" --output text)
  if vacio "$id"; then
    id=$(aws apigatewayv2 create-api --name "$1" --protocol-type HTTP --query ApiId --output text)
    ok "API $1 creada ($id)"
  else
    ok "API $1 ya existe ($id)"
  fi
  if ! aws apigatewayv2 get-stage --api-id "$id" --stage-name '$default' >/dev/null 2>&1; then
    aws apigatewayv2 create-stage --api-id "$id" --stage-name '$default' --auto-deploy >/dev/null
  fi
  echo "$id"
}

integracion() { # api_id nombre_lambda -> id de integración (reutiliza una existente)
  local uri="arn:aws:lambda:${REGION}:${CUENTA}:function:$2"
  local id
  id=$(aws apigatewayv2 get-integrations --api-id "$1" \
        --query "Items[?IntegrationUri=='${uri}'].IntegrationId | [0]" --output text)
  if vacio "$id"; then
    id=$(aws apigatewayv2 create-integration --api-id "$1" --integration-type AWS_PROXY \
          --integration-uri "$uri" --payload-format-version 2.0 --query IntegrationId --output text)
  fi
  # Permiso para que la API invoque la Lambda (se ignora si ya existe)
  aws lambda add-permission --function-name "$2" --statement-id "apigw-$1" \
    --action lambda:InvokeFunction --principal apigateway.amazonaws.com \
    --source-arn "arn:aws:execute-api:${REGION}:${CUENTA}:$1/*/*" >/dev/null 2>&1 || true
  echo "$id"
}

ruta() { # api_id "METODO /ruta" integracion
  local existe
  existe=$(aws apigatewayv2 get-routes --api-id "$1" --query "Items[?RouteKey=='$2'].RouteId | [0]" --output text)
  if vacio "$existe"; then
    aws apigatewayv2 create-route --api-id "$1" --route-key "$2" --target "integrations/$3" >/dev/null
    ok "Ruta $2"
  else
    # Si la ruta existe pero sin integración, se le asigna
    aws apigatewayv2 update-route --api-id "$1" --route-id "$existe" --target "integrations/$3" >/dev/null
    ok "Ruta $2 (ya existía)"
  fi
}

paso "API apiEquipajes"
API_EQ=$(crear_api apiEquipajes)
INT_EQ=$(integracion "$API_EQ" funEquipajes)
ruta "$API_EQ" "GET /equipajes"         "$INT_EQ"
ruta "$API_EQ" "GET /equipajes/{id}"    "$INT_EQ"
ruta "$API_EQ" "POST /equipajes"        "$INT_EQ"
ruta "$API_EQ" "PUT /equipajes/{id}"    "$INT_EQ"
ruta "$API_EQ" "DELETE /equipajes/{id}" "$INT_EQ"

paso "API apiDestinos"
API_DE=$(crear_api apiDestinos)
INT_DE=$(integracion "$API_DE" funDestinos)
ruta "$API_DE" "GET /destinos" "$INT_DE"

paso "API apiAuth"
API_AU=$(crear_api apiAuth)
INT_AU=$(integracion "$API_AU" funAuth)
ruta "$API_AU" "POST /auth/login" "$INT_AU"

# ----------------------------------------------------------------- Datos ----
paso "Datos iniciales"
aws dynamodb batch-write-item --request-items file://datos/destinos.json >/dev/null && ok "10 destinos"
aws dynamodb batch-write-item --request-items file://datos/usuarios.json >/dev/null && ok "3 usuarios de prueba (contraseña: CruzDelSur2026)"
if [ "$CARGAR_EJEMPLOS" = "--ejemplos" ]; then
  aws dynamodb batch-write-item --request-items file://datos/equipajes-ejemplo.json >/dev/null && ok "4 equipajes de ejemplo"
fi

# ---------------------------------------------------------------- Prueba ----
URL_EQ="https://${API_EQ}.execute-api.${REGION}.amazonaws.com/"
URL_DE="https://${API_DE}.execute-api.${REGION}.amazonaws.com/"
URL_AU="https://${API_AU}.execute-api.${REGION}.amazonaws.com/"
paso "Prueba rápida"
sleep 3
if curl -fsS "${URL_DE}destinos" | grep -q '"success": *true'; then ok "GET /destinos responde"; else echo "    ⚠ GET /destinos no respondió todavía" >&2; fi
if curl -fsS "${URL_EQ}equipajes" | grep -q '"success": *true'; then ok "GET /equipajes responde"; else echo "    ⚠ GET /equipajes no respondió todavía" >&2; fi
if curl -fsS -X POST "${URL_AU}auth/login" -H "Content-Type: application/json" \
     -d '{"codigo":"OP-4821","password":"CruzDelSur2026"}' | grep -q '"token"'; then ok "Login OP-4821 funciona"; else echo "    ⚠ El login de prueba no respondió como se esperaba" >&2; fi

paso "LISTO: copia estas URLs en NetworkModule.kt"
printf '    EQUIPAJES_BASE_URL = "%s"\n    DESTINOS_BASE_URL  = "%s"\n    AUTH_BASE_URL      = "%s"\n\n' "$URL_EQ" "$URL_DE" "$URL_AU"
