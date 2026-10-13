"""
Lambda: funEquipajes  (Python 3.13)
Cruz del Sur - Trazabilidad de equipaje de bodega (Grupo 6)

Rutas que consume la app Android (EquipajeApiService):
  GET    /equipajes          -> lista de unidades de equipaje
  GET    /equipajes/{id}     -> detalle de una unidad
  POST   /equipajes          -> registra una unidad
  PUT    /equipajes/{id}     -> actualiza una unidad
  DELETE /equipajes/{id}     -> elimina una unidad

Respuesta (ApiResponseDto): { success, data, timestamp }
Cuerpo de POST/PUT (RegistrarEquipajeDto): { pasajero, boleto, pesoKg, destino, observaciones }

Seguridad: GET es público (consulta del pasajero). POST, PUT y DELETE exigen
"Authorization: Bearer <token>" emitido por funAuth (operario con sesión).

Variables de entorno:
  TABLA_EQUIPAJES = cds-equipajes
  TOKEN_SECRET    = la misma clave configurada en funAuth
"""
import base64
import hashlib
import hmac
import json
import os
import re
import time
import uuid
from datetime import datetime, timezone
from decimal import Decimal

import boto3
from botocore.exceptions import ClientError

TABLA = os.environ.get("TABLA_EQUIPAJES", "cds-equipajes")
SECRETO = os.environ.get("TOKEN_SECRET", "")
ESTADO_INICIAL = "EN_BODEGA"

tabla = boto3.resource("dynamodb").Table(TABLA)


class ErrorCliente(Exception):
    """Datos inválidos enviados por la app (400)."""


class ErrorSesion(Exception):
    """Falta el token o no es válido (401)."""


# ---------------------------------------------------------------- Respuestas
def _ahora():
    return datetime.now(timezone.utc).isoformat(timespec="milliseconds").replace("+00:00", "Z")


def _json_default(valor):
    # DynamoDB devuelve los números como Decimal
    if isinstance(valor, Decimal):
        return int(valor) if valor == valor.to_integral_value() else float(valor)
    raise TypeError(f"Tipo no serializable: {type(valor)}")


def responder(status, cuerpo):
    return {
        "statusCode": status,
        "headers": {"Content-Type": "application/json; charset=utf-8"},
        "body": json.dumps(cuerpo, default=_json_default, ensure_ascii=False),
    }


def ok(data, status=200):
    return responder(status, {"success": True, "data": data, "timestamp": _ahora()})


def fallo(status, mensaje):
    return responder(status, {"success": False, "data": None, "message": mensaje, "timestamp": _ahora()})


def a_dto(item):
    """Unidad devuelta a la app (EquipajeDto)."""
    campos = ["id", "pasajero", "boleto", "pesoKg", "destino", "observaciones", "codigoRastreo",
              "estado", "fechaRegistro", "fechaActualizacion", "registradoPor", "actualizadoPor"]
    return {c: item.get(c) for c in campos}


# ---------------------------------------------------- Sesión del operario
def _b64url_decode(texto):
    return base64.urlsafe_b64decode(texto + "=" * (-len(texto) % 4))


def operario_autenticado(event):
    """Valida el token de funAuth y devuelve sus datos { sub, nombre, rol, exp }."""
    if not SECRETO:
        raise RuntimeError("Falta la variable de entorno TOKEN_SECRET")
    headers = {k.lower(): v for k, v in (event.get("headers") or {}).items()}
    auth = headers.get("authorization", "")
    token = auth[7:].strip() if auth.startswith("Bearer ") else ""
    partes = token.split(".")
    if len(partes) != 2 or not all(partes):
        raise ErrorSesion("Debe iniciar sesión como operario")
    cuerpo, firma = partes
    esperada = hmac.new(SECRETO.encode(), cuerpo.encode(), hashlib.sha256).digest()
    try:
        recibida = _b64url_decode(firma)
    except Exception:
        raise ErrorSesion("Sesión inválida, vuelva a iniciar sesión")
    if not hmac.compare_digest(recibida, esperada):
        raise ErrorSesion("Sesión inválida, vuelva a iniciar sesión")
    payload = json.loads(_b64url_decode(cuerpo))
    if not payload.get("exp") or payload["exp"] < int(time.time()):
        raise ErrorSesion("Sesión expirada, vuelva a iniciar sesión")
    return payload


# ---------------------------------------------------------- Utilidades
def codigo_rastreo(id_, destino):
    """Mismo código que muestra la app: CDS-<3 letras del destino>-<últimos 6 del id>."""
    letras = "".join(c for c in destino if c.isalpha())[:3].upper() or "GEN"
    sufijo = re.sub(r"[^a-zA-Z0-9]", "", id_)[-6:].upper() or "000000"
    return f"CDS-{letras}-{sufijo}"


def leer_cuerpo(event):
    """Valida el cuerpo RegistrarEquipajeDto."""
    texto = event.get("body")
    if not texto:
        raise ErrorCliente("El cuerpo de la solicitud es obligatorio")
    if event.get("isBase64Encoded"):
        texto = base64.b64decode(texto).decode("utf-8")
    try:
        b = json.loads(texto)
    except json.JSONDecodeError:
        raise ErrorCliente("El cuerpo no es un JSON válido")

    def requerido(campo):
        valor = b.get(campo)
        if not isinstance(valor, str) or not valor.strip():
            raise ErrorCliente(f"El campo {campo} es obligatorio")
        return valor.strip()

    try:
        peso = int(b.get("pesoKg"))
        if peso != float(b.get("pesoKg")):
            raise ValueError
    except (TypeError, ValueError):
        peso = 0
    if peso <= 0:
        raise ErrorCliente("El campo pesoKg debe ser un entero mayor que 0")

    return {
        "pasajero": requerido("pasajero"),
        "boleto": requerido("boleto"),
        "pesoKg": peso,
        "destino": requerido("destino"),
        "observaciones": requerido("observaciones"),
    }


def _es_condicion_fallida(error):
    return error.response.get("Error", {}).get("Code") == "ConditionalCheckFailedException"


# ---------------------------------------------------------- Operaciones
def listar(qs):
    limite = None
    if qs.get("limit"):
        try:
            limite = min(max(int(qs["limit"]), 1), 100)
        except ValueError:
            raise ErrorCliente("limit inválido")
    clave = None
    if qs.get("nextKey"):
        try:
            clave = json.loads(_b64url_decode(qs["nextKey"]))
        except Exception:
            raise ErrorCliente("nextKey inválido")

    # Sin "limit" se recorre toda la tabla (la app no pagina)
    items = []
    while True:
        params = {}
        if limite:
            params["Limit"] = limite
        if clave:
            params["ExclusiveStartKey"] = clave
        r = tabla.scan(**params)
        items.extend(r.get("Items", []))
        clave = r.get("LastEvaluatedKey")
        if not clave or limite:
            break

    items.sort(key=lambda x: x.get("fechaRegistro") or "", reverse=True)
    siguiente = None
    if clave:
        siguiente = base64.urlsafe_b64encode(json.dumps(clave).encode()).decode().rstrip("=")
    return ok({"items": [a_dto(i) for i in items], "nextKey": siguiente})


def obtener(id_):
    item = tabla.get_item(Key={"id": id_}).get("Item")
    if not item:
        return fallo(404, "Unidad de equipaje no encontrada")
    return ok(a_dto(item))


def registrar(event, operario):
    datos = leer_cuerpo(event)
    ahora = _ahora()
    id_ = str(uuid.uuid4())
    item = {
        "id": id_,
        **datos,
        "codigoRastreo": codigo_rastreo(id_, datos["destino"]),
        "estado": ESTADO_INICIAL,
        "fechaRegistro": ahora,
        "fechaActualizacion": ahora,
        "registradoPor": operario["sub"],
    }
    tabla.put_item(Item=item, ConditionExpression="attribute_not_exists(id)")
    return ok(a_dto(item), 201)


def actualizar(id_, event, operario):
    d = leer_cuerpo(event)
    try:
        r = tabla.update_item(
            Key={"id": id_},
            ConditionExpression="attribute_exists(id)",
            UpdateExpression=("SET pasajero = :p, boleto = :b, pesoKg = :k, destino = :d, observaciones = :o, "
                              "codigoRastreo = :c, fechaActualizacion = :f, actualizadoPor = :u"),
            ExpressionAttributeValues={
                ":p": d["pasajero"], ":b": d["boleto"], ":k": d["pesoKg"], ":d": d["destino"],
                ":o": d["observaciones"], ":c": codigo_rastreo(id_, d["destino"]),
                ":f": _ahora(), ":u": operario["sub"],
            },
            ReturnValues="ALL_NEW",
        )
        return ok(a_dto(r["Attributes"]))
    except ClientError as e:
        if _es_condicion_fallida(e):
            return fallo(404, "Unidad de equipaje no encontrada")
        raise


def eliminar(id_):
    try:
        tabla.delete_item(Key={"id": id_}, ConditionExpression="attribute_exists(id)")
        return ok({"id": id_})
    except ClientError as e:
        if _es_condicion_fallida(e):
            return fallo(404, "Unidad de equipaje no encontrada")
        raise


# ------------------------------------------- Enrutador (HTTP API v2 / REST v1)
def lambda_handler(event, context):
    metodo = (event.get("requestContext", {}).get("http", {}) or {}).get("method") or event.get("httpMethod")
    id_ = (event.get("pathParameters") or {}).get("id")

    try:
        if metodo == "GET" and not id_:
            return listar(event.get("queryStringParameters") or {})
        if metodo == "GET" and id_:
            return obtener(id_)
        if metodo == "POST" and not id_:
            return registrar(event, operario_autenticado(event))
        if metodo == "PUT" and id_:
            return actualizar(id_, event, operario_autenticado(event))
        if metodo == "DELETE" and id_:
            operario_autenticado(event)
            return eliminar(id_)
        return fallo(405, f"Operación no soportada: {metodo}")
    except ErrorCliente as e:
        return fallo(400, str(e))
    except ErrorSesion as e:
        return fallo(401, str(e))
    except Exception as e:  # noqa: BLE001
        print(f"Error no controlado: {e!r}")
        return fallo(500, "Error interno del servidor")
