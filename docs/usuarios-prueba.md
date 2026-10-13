# Usuarios de prueba

Operarios registrados en la tabla DynamoDB `cds-usuarios` (AWS Academy Learner Lab, `us-east-1`). Sirven para iniciar sesión en la app y probar el registro, la edición y la eliminación de equipaje.

| Código de colaborador | Nombre | Rol (`rol` en la tabla) | Rol en la app | Terminal | Contraseña |
| --- | --- | --- | --- | --- | --- |
| `OP-4821` | Carlos Mendoza | `OPERARIO_BODEGA` | Operario de bodega | Terminal Javier Prado - Lima | `CruzDelSur2026` |
| `OP-1020` | Julia Salazar | `OPERARIO_COUNTER` | Operario de counter | Terminal Javier Prado - Lima | `CruzDelSur2026` |
| `SP-0001` | Supervisor Operativo | `SUPERVISOR` | Supervisor | Terminal Javier Prado - Lima | `CruzDelSur2026` |

- El pasajero no necesita usuario: en el inicio de sesión usa **«Consultar mi equipaje»**.
- En la tabla la contraseña no se guarda en texto plano, sino como `passwordHash` (`scrypt$<sal>$<hash>`). La función `funAuth` la verifica y entrega un token válido por 12 horas.
- Los registros listos para cargar están en [`aws-backend/datos/usuarios.json`](../aws-backend/datos/usuarios.json).

## Cargar los usuarios en DynamoDB

```bash
aws dynamodb batch-write-item --region us-east-1 --request-items file://aws-backend/datos/usuarios.json
```

## Crear un usuario nuevo

```bash
cd aws-backend
python3 scripts/crear_usuario.py OP-5001 "Ana Torres" OPERARIO_COUNTER "Terminal Javier Prado - Lima" "MiClave2026" > usuario.json
aws dynamodb put-item --region us-east-1 --table-name cds-usuarios --item file://usuario.json
```

## Probar el inicio de sesión

```bash
curl -X POST https://kcmfwaje63.execute-api.us-east-1.amazonaws.com/auth/login \
  -H "Content-Type: application/json" \
  -d '{"codigo":"OP-4821","password":"CruzDelSur2026"}'
```

Respuesta esperada: `200` con `{ codigo, nombre, rol, terminal, token, expiraEn }`. Con una contraseña incorrecta responde `401`.

> Son credenciales solo para pruebas académicas. No las uses para datos reales.
