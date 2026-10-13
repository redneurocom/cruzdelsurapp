"""
Genera un usuario para la tabla cds-usuarios con la contraseña cifrada (scrypt).

Uso:
  python3 scripts/crear_usuario.py <codigo> "<nombre>" <rol> "<terminal>" "<password>" > usuario.json
  aws dynamodb put-item --table-name cds-usuarios --item file://usuario.json

Roles sugeridos: OPERARIO_COUNTER, OPERARIO_BODEGA, SUPERVISOR
"""
import base64
import hashlib
import json
import os
import sys


def hash_password(password):
    salt = os.urandom(16)
    h = hashlib.scrypt(password.encode(), salt=salt, n=16384, r=8, p=1, dklen=32)
    return f"scrypt${base64.b64encode(salt).decode()}${base64.b64encode(h).decode()}"


if len(sys.argv) != 6:
    sys.exit('Uso: python3 scripts/crear_usuario.py <codigo> "<nombre>" <rol> "<terminal>" "<password>"')
codigo, nombre, rol, terminal, password = sys.argv[1:]
print(json.dumps({
    "codigo": {"S": codigo.strip().upper()},
    "nombre": {"S": nombre},
    "rol": {"S": rol},
    "terminal": {"S": terminal},
    "activo": {"BOOL": True},
    "passwordHash": {"S": hash_password(password)},
}, indent=2, ensure_ascii=False))
