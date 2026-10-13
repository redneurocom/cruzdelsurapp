CRUZ DEL SUR - MI EQUIPAJE
Configuración del backend en AWS (misma estructura del proyecto librosApp del curso)

1. TABLAS DYNAMODB (clave de partición: id - String)
   - equipajes   -> campos: id, boleto, pasajero, destino, tipo, peso, observaciones
   - destinos    -> campos: id, nombre
     Datos iniciales de destinos: Lima, Arequipa, Cusco, Trujillo, Chiclayo, Puno

2. FUNCIONES LAMBDA (Python, rol LabRole)
   - funEquipajes -> código: lambda_equipajes.py  (variable de entorno TABLE_NAME=equipajes)
   - funDestinos  -> código: lambda_destinos.py   (variable de entorno TABLE_NAME=destinos)

3. API GATEWAY (HTTP API, integración Lambda, stage $default)
   apiEquipajes -> funEquipajes
     GET    /equipajes
     GET    /equipajes/{id}
     POST   /equipajes
     PUT    /equipajes/{id}
     DELETE /equipajes/{id}

   apiDestinos -> funDestinos
     GET    /destinos
     GET    /destinos/{id}
     POST   /destinos
     PUT    /destinos/{id}
     DELETE /destinos/{id}

4. URLs usadas en la app (di/NetworkModule.kt)
   EQUIPAJES_BASE_URL = https://wwwbnjtcae.execute-api.us-east-1.amazonaws.com/
   DESTINOS_BASE_URL  = https://op1xpdid16.execute-api.us-east-1.amazonaws.com/
   Si se crean nuevas APIs, reemplazar estas URLs en NetworkModule.kt.

5. Ejemplo de cuerpo para POST /equipajes
   {
     "boleto": "B001-0029412",
     "pasajero": "Elena Morales Vargas",
     "destino": "Arequipa",
     "tipo": "Maleta",
     "peso": 18,
     "observaciones": "Frágil, con funda protectora"
   }
