# Backend AWS — Cruz del Sur, trazabilidad de equipaje (Grupo 6)

Backend serverless de la app `appmovile.grupo6`, desplegado en **AWS Academy Learner Lab** (región `us-east-1`):

**API Gateway (HTTP API) → AWS Lambda (Python 3.13, rol LabRole) → Amazon DynamoDB**

```
aws-backend/
├── lambdas/
│   ├── funEquipajes/lambda_function.py   # registrar, listar, ver, editar y eliminar equipajes
│   ├── funDestinos/lambda_function.py    # catálogo de destinos
│   └── funAuth/lambda_function.py        # inicio de sesión de operarios
├── datos/
│   ├── destinos.json                     # 10 destinos (Arequipa, Cusco, Trujillo…)
│   ├── usuarios.json                     # 3 operarios de prueba (contraseña cifrada)
│   └── equipajes-ejemplo.json            # 4 equipajes de ejemplo (opcional)
└── scripts/
    └── crear_usuario.py                  # genera un usuario nuevo con su contraseña cifrada
```

---

## 1. Tablas DynamoDB (capacidad bajo demanda)

| Tabla | Clave de partición | Atributos |
| --- | --- | --- |
| `cds-equipajes` | `id` (S) | `pasajero`, `boleto`, `pesoKg` (N), `destino`, `observaciones`, `codigoRastreo`, `estado`, `fechaRegistro`, `fechaActualizacion`, `registradoPor`, `actualizadoPor` |
| `cds-destinos` | `id` (S) | `nombre`, `orden` (N), `activo` (BOOL) |
| `cds-usuarios` | `codigo` (S) | `nombre`, `rol`, `terminal`, `activo` (BOOL), `passwordHash` |

- `codigoRastreo` = `CDS-<3 letras del destino>-<últimos 6 caracteres del id>`, el mismo código que muestra la app.
- `passwordHash` = `scrypt$<sal>$<hash>`. La contraseña nunca se guarda en texto plano.

## 2. Funciones Lambda (Python 3.13, rol **LabRole**, handler `lambda_function.lambda_handler`, timeout 10 s)

| Función | Variables de entorno |
| --- | --- |
| `funEquipajes` | `TABLA_EQUIPAJES` = `cds-equipajes`, `TOKEN_SECRET` = clave secreta |
| `funDestinos` | `TABLA_DESTINOS` = `cds-destinos` |
| `funAuth` | `TABLA_USUARIOS` = `cds-usuarios`, `TOKEN_SECRET` = **la misma** clave secreta |

`boto3` ya viene incluido en el runtime de Python: no hay dependencias que instalar.

## 3. APIs HTTP (etapa `$default` con implementación automática)

| API | Integración | Rutas | Constante en `NetworkModule.kt` |
| --- | --- | --- | --- |
| `apiEquipajes` | funEquipajes | `GET /equipajes`, `GET /equipajes/{id}`, `POST /equipajes`, `PUT /equipajes/{id}`, `DELETE /equipajes/{id}` | `EQUIPAJES_BASE_URL` |
| `apiDestinos` | funDestinos | `GET /destinos` | `DESTINOS_BASE_URL` |
| `apiAuth` | funAuth | `POST /auth/login` | `AUTH_BASE_URL` |

Todas las respuestas tienen la forma `{ "success": true|false, "data": ..., "message"?: ..., "timestamp": ... }`.

| Ruta | Cuerpo | Respuesta |
| --- | --- | --- |
| `POST /auth/login` | `{ "codigo": "OP-4821", "password": "..." }` | 200 con `{ codigo, nombre, rol, terminal, token, expiraEn }` · 401 credenciales incorrectas |
| `GET /destinos` | — | 200 con `{ items: [{ id, nombre }], nextKey: null }` |
| `GET /equipajes` | — | 200 con `{ items: [...], nextKey }` |
| `GET /equipajes/{id}` | — | 200 con la unidad · 404 si no existe |
| `POST /equipajes` | `{ "pasajero", "boleto", "pesoKg", "destino", "observaciones" }` | 201 con la unidad · 400 datos inválidos |
| `PUT /equipajes/{id}` | igual que POST | 200 con la unidad actualizada · 404 si no existe |
| `DELETE /equipajes/{id}` | — | 200 con `{ id }` · 404 si no existe |

**Seguridad:** las consultas (GET) son públicas, porque el pasajero consulta su equipaje sin cuenta. `POST`, `PUT` y `DELETE` exigen el encabezado `Authorization: Bearer <token>` que entrega `/auth/login`. El token dura 12 horas; sin token, o con uno vencido o alterado, la respuesta es 401. La app envía el token sola después del login.

## 4. Usuarios de prueba

| Código | Nombre | Rol | Contraseña |
| --- | --- | --- | --- |
| `OP-4821` | Carlos Mendoza | Operario de bodega | `CruzDelSur2026` |
| `OP-1020` | Julia Salazar | Operario de counter | `CruzDelSur2026` |
| `SP-0001` | Supervisor Operativo | Supervisor | `CruzDelSur2026` |

Para agregar un usuario:

```bash
python3 scripts/crear_usuario.py OP-5001 "Ana Torres" OPERARIO_COUNTER "Terminal Javier Prado - Lima" "MiClave2026" > usuario.json
aws dynamodb put-item --region us-east-1 --table-name cds-usuarios --item file://usuario.json
```

## 5. Creación de los servicios

Las tablas, las funciones Lambda y las APIs se crearon **manualmente desde la consola de AWS** con los nombres y la configuración de las secciones 1 a 3. El código de cada Lambda se copia desde `lambdas/<función>/lambda_function.py`, y las variables de entorno se configuran en la pestaña *Configuración* de cada función.

Para cargar los datos iniciales desde CloudShell (dentro de esta carpeta):

```bash
aws dynamodb batch-write-item --region us-east-1 --request-items file://datos/destinos.json
aws dynamodb batch-write-item --region us-east-1 --request-items file://datos/usuarios.json
aws dynamodb batch-write-item --region us-east-1 --request-items file://datos/equipajes-ejemplo.json   # opcional
```

## 6. Probar

```bash
curl https://<apiDestinos>.execute-api.us-east-1.amazonaws.com/destinos
curl -X POST https://<apiAuth>.execute-api.us-east-1.amazonaws.com/auth/login \
  -H "Content-Type: application/json" -d '{"codigo":"OP-4821","password":"CruzDelSur2026"}'
```

Los errores de cada función se ven en CloudWatch Logs: `/aws/lambda/funEquipajes`, `/aws/lambda/funDestinos` y `/aws/lambda/funAuth`.
