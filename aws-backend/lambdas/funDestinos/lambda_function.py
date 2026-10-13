"""
Lambda: funDestinos  (Python 3.13)
Cruz del Sur - Trazabilidad de equipaje de bodega (Grupo 6)

Ruta que consume la app Android (DestinoApiService):
  GET /destinos -> catálogo de destinos (terminales) para el registro de equipaje

Respuesta (ApiResponseDto<DestinosDataDto>):
  { success, data: { items: [{ id, nombre }], nextKey }, timestamp }

Variable de entorno:
  TABLA_DESTINOS = cds-destinos
"""
import json
import os
from datetime import datetime, timezone

import boto3

TABLA = os.environ.get("TABLA_DESTINOS", "cds-destinos")
tabla = boto3.resource("dynamodb").Table(TABLA)


def _ahora():
    return datetime.now(timezone.utc).isoformat(timespec="milliseconds").replace("+00:00", "Z")


def responder(status, cuerpo):
    return {
        "statusCode": status,
        "headers": {"Content-Type": "application/json; charset=utf-8"},
        "body": json.dumps(cuerpo, ensure_ascii=False),
    }


def lambda_handler(event, context):
    metodo = (event.get("requestContext", {}).get("http", {}) or {}).get("method") or event.get("httpMethod")
    if metodo != "GET":
        return responder(405, {"success": False, "data": None,
                               "message": f"Operación no soportada: {metodo}", "timestamp": _ahora()})
    try:
        items, clave = [], None
        while True:
            r = tabla.scan(**({"ExclusiveStartKey": clave} if clave else {}))
            items.extend(r.get("Items", []))
            clave = r.get("LastEvaluatedKey")
            if not clave:
                break

        activos = [d for d in items if d.get("activo") is not False]
        activos.sort(key=lambda d: (int(d.get("orden", 999)), str(d.get("nombre", ""))))
        destinos = [{"id": d.get("id"), "nombre": d.get("nombre")} for d in activos]

        return responder(200, {"success": True, "data": {"items": destinos, "nextKey": None},
                               "timestamp": _ahora()})
    except Exception as e:  # noqa: BLE001
        print(f"Error al listar destinos: {e!r}")
        return responder(500, {"success": False, "data": None,
                               "message": "Error interno del servidor", "timestamp": _ahora()})
