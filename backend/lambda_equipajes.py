import json
import os
import uuid
from datetime import datetime, timezone
from decimal import Decimal
import boto3
from boto3.dynamodb.conditions import Key

# Inicialización de DynamoDB
dynamodb = boto3.resource('dynamodb')
TABLE_NAME = os.environ.get('TABLE_NAME', 'equipajes')
table = dynamodb.Table(TABLE_NAME)

# Helper para serializar tipos de datos de DynamoDB (Decimal, etc.)
class DecimalEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, Decimal):
            return int(obj) if obj % 1 == 0 else float(obj)
        return super(DecimalEncoder, self).default(obj)

def get_timestamp():
    return datetime.now(timezone.utc).strftime('%Y-%m-%dT%H:%M:%S.%f')[:-3] + 'Z'

def build_response(status_code, body=None):
    response = {
        'statusCode': status_code,
        'headers': {
            'Content-Type': 'application/json'
        }
    }
    if body is not None:
        response['body'] = json.dumps(body, cls=DecimalEncoder)
    return response

def lambda_handler(event, context):
    try:
        # Extraer método y ruta desde HTTP API (v2 payload)
        http_context = event.get('requestContext', {}).get('http', {})
        http_method = http_context.get('method')
        
        # Extraer parámetros de ruta ({id})
        path_parameters = event.get('pathParameters') or {}
        equipaje_id = path_parameters.get('id')
        
        # 1. LISTAR (GET /equipajes)
        if http_method == 'GET' and not equipaje_id:
            response = table.scan()
            items = response.get('Items', [])
            
            body = {
                "success": True,
                "data": {
                    "items": items,
                    "nextKey": response.get('LastEvaluatedKey', None)
                },
                "timestamp": get_timestamp()
            }
            return build_response(200, body)

        # 2. BUSCAR POR ID (GET /equipajes/{id})
        elif http_method == 'GET' and equipaje_id:
            response = table.get_item(Key={'id': equipaje_id})
            item = response.get('Item')
            
            if not item:
                return build_response(404, {
                    "success": False,
                    "message": "Equipaje no encontrado",
                    "timestamp": get_timestamp()
                })
                
            body = {
                "success": True,
                "data": item,
                "timestamp": get_timestamp()
            }
            return build_response(200, body)

        # 3. INSERTAR (POST /equipajes)
        elif http_method == 'POST':
            data = json.loads(event.get('body', '{}'))
            generated_id = str(uuid.uuid4())
            
            item = {
                "id": generated_id,
                "boleto": data.get('boleto'),
                "pasajero": data.get('pasajero'),
                "destino": data.get('destino'),
                "tipo": data.get('tipo'),
                "peso": data.get('peso'),
                "observaciones": data.get('observaciones')
            }
            
            table.put_item(Item=item)
            
            body = {
                "success": True,
                "data": item,
                "timestamp": get_timestamp()
            }
            return build_response(201, body)

        # 4. ACTUALIZAR (PUT /equipajes/{id})
        elif http_method == 'PUT' and equipaje_id:
            data = json.loads(event.get('body', '{}'))
            
            item = {
                "id": equipaje_id,
                "boleto": data.get('boleto'),
                "pasajero": data.get('pasajero'),
                "destino": data.get('destino'),
                "tipo": data.get('tipo'),
                "peso": data.get('peso'),
                "observaciones": data.get('observaciones')
            }
            
            table.put_item(Item=item)
            
            body = {
                "success": True,
                "data": item,
                "timestamp": get_timestamp()
            }
            return build_response(200, body)

        # 5. ELIMINAR (DELETE /equipajes/{id})
        elif http_method == 'DELETE' and equipaje_id:
            table.delete_item(Key={'id': equipaje_id})
            return build_response(204)

        else:
            return build_response(400, {
                "success": False,
                "message": "Ruta o método no soportado",
                "timestamp": get_timestamp()
            })

    except Exception as e:
        return build_response(500, {
            "success": False,
            "error": str(e),
            "timestamp": get_timestamp()
        })
