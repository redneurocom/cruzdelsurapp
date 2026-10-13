"""
Lambda: funAuth  (Python 3.13)
Cruz del Sur - Trazabilidad de equipaje de bodega (Grupo 6)

Ruta que consume la app Android (AuthApiService):
  POST /auth/login   cuerpo { codigo, password }

Respuesta correcta (200, ApiResponseDto<UsuarioDto>):
  { success: true, data: { codigo, nombre, rol, terminal, token, expiraEn }, timestamp }
Credenciales incorrectas o usuario inactivo: 401.

Tabla cds-usuarios (clave: codigo):
  codigo, nombre, rol, terminal, activo, passwordHash
  passwordHash = "scrypt$<salt base64>$<hash base64>"  (N=16384, r=8, p=1, 32 bytes)

Variables de entorno:
  TABLA_USUARIOS = cds-usuarios
  TOKEN_SECRET   = clave secreta (la misma en funEquipajes)
"""
import base64
import hashlib
import hmac
import json
import os
import time
from datetime import datetime, timezone

import boto3

TABLA = os.environ.get("TABLA_USUARIOS", "cds-usuarios")
SECRETO = os.environ.get("TOKEN_SECRET", "")
DURACION_HORAS = 12

tabla = boto3.resource("dynamodb").Table(TABLA)


def _ahora():
    return datetime.now(timezone.utc).isoformat(timespec="milliseconds").replace("+00:00", "Z")


def responder(status, cuerpo):
    return {
        "statusCode": status,
        "headers": {"Content-Type": "application/json; charset=utf-8"},
        "body": json.dumps(cuerpo, ensure_ascii=False),
    }


def ok(data):
    return responder(200, {"success": True, "data": data, "timestamp": _ahora()})


def fallo(status, mensaje):
    return responder(status, {"success": False, "data": None, "message": mensaje, "timestamp": _ahora()})


def verificar_password(password, password_hash):
    """Compara la contraseña con el hash guardado (tiempo constante)."""
    try:
        algoritmo, salt, hash_b64 = str(password_hash or "").split("$")
    except ValueError:
        return False
    if algoritmo != "scrypt" or not salt or not hash_b64:
        return False
    esperado = base64.b64decode(hash_b64)
    calculado = hashlib.scrypt(password.encode(), salt=base64.b64decode(salt),
                               n=16384, r=8, p=1, dklen=len(esperado))
    return hmac.compare_digest(calculado, esperado)


def _b64url(datos):
    return base64.urlsafe_b64encode(datos).decode().rstrip("=")


def firmar_token(payload, secreto):
    """Token: base64url(payload).base64url(firma HMAC-SHA256)."""
    cuerpo = _b64url(json.dumps(payload, separators=(",", ":"), ensure_ascii=False).encode())
    firma = _b64url(hmac.new(secreto.encode(), cuerpo.encode(), hashlib.sha256).digest())
    return f"{cuerpo}.{firma}"


def lambda_handler(event, context):
    metodo = (event.get("requestContext", {}).get("http", {}) or {}).get("method") or event.get("httpMethod")
    if metodo != "POST":
        return fallo(405, f"Operación no soportada: {metodo}")
    if not SECRETO:
        print("Falta la variable de entorno TOKEN_SECRET")
        return fallo(500, "Servicio de autenticación no configurado")

    try:
        texto = event.get("body") or "{}"
        if event.get("isBase64Encoded"):
            texto = base64.b64decode(texto).decode("utf-8")
        cuerpo = json.loads(texto)
    except (ValueError, json.JSONDecodeError):
        return fallo(400, "El cuerpo no es un JSON válido")

    codigo = str(cuerpo.get("codigo") or "").strip().upper()
    password = str(cuerpo.get("password") or "")
    if not codigo or not password:
        return fallo(400, "Ingrese su código de colaborador y contraseña")

    try:
        usuario = tabla.get_item(Key={"codigo": codigo}).get("Item")
        # Mismo mensaje para usuario inexistente o contraseña errónea (no revela cuál falló)
        if (not usuario or usuario.get("activo") is False
                or not verificar_password(password, usuario.get("passwordHash"))):
            return fallo(401, "Código de colaborador o contraseña incorrectos")

        expira_en = int(time.time()) + DURACION_HORAS * 3600
        token = firmar_token({"sub": usuario["codigo"], "nombre": usuario.get("nombre"),
                              "rol": usuario.get("rol"), "exp": expira_en}, SECRETO)
        return ok({
            "codigo": usuario["codigo"],
            "nombre": usuario.get("nombre"),
            "rol": usuario.get("rol"),
            "terminal": usuario.get("terminal"),
            "token": token,
            "expiraEn": expira_en,
        })
    except Exception as e:  # noqa: BLE001
        print(f"Error en login: {e!r}")
        return fallo(500, "Error interno del servidor")
