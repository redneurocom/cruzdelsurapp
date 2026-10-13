# Cruz del Sur: trazabilidad de equipaje de bodega

Este repositorio reúne el avance del proyecto desarrollado para el curso IS276, Plataformas Móviles y Análisis Cloud, de la Universidad Peruana de Ciencias Aplicadas.

La propuesta consiste en una aplicación móvil Android para registrar y consultar la trazabilidad del equipaje de bodega en la Empresa de Transportes Cruz del Sur S.A. Cada unidad de equipaje se identifica mediante un código único y una etiqueta QR. A partir de esa identificación, el personal puede registrar los eventos de recepción, carga, escala y entrega, mientras que el pasajero puede consultar el estado y el historial de su equipaje.

## Estado actual

Actividades completadas en el trabajo parcial:

- Delimitación del problema y del proceso de equipaje de bodega.
- Definición del objetivo general, los objetivos específicos y el alcance.
- Elaboración de diez historias de usuario con sus criterios de aceptación.
- Definición de la arquitectura propuesta con MVVM y servicios de AWS.
- Diseño del prototipo de alta fidelidad con doce pantallas.
- Organización del seguimiento de actividades en un tablero Kanban.
- Elaboración y revisión del informe del trabajo parcial.

### Implementación (etapa actual)

La aplicación Android y su backend en AWS ya cubren el flujo principal:

| Funcionalidad | Estado |
| --- | --- |
| Inicio de sesión del operario y cierre de sesión | Implementado (Lambda `funAuth`, token de sesión) |
| Registro, edición y eliminación de equipaje | Implementado (Lambda `funEquipajes`; exige sesión) |
| Manifiesto de bodega agrupado por destino | Implementado |
| Consulta del pasajero por nombre, boleto o código `CDS-…` | Implementado (sin iniciar sesión) |
| Catálogo de destinos | Implementado (Lambda `funDestinos`) |
| Recuperación de contraseña | Pendiente (la app indica contactar al supervisor) |
| Lectura de código QR y eventos de carga, escala y entrega | Pendiente |
| Historial del equipaje y reporte de incidencias | Pendiente |

## Estructura del repositorio

```
app/            Aplicación Android (Kotlin, Jetpack Compose, MVVM, Hilt, Retrofit)
aws-backend/    Lambdas en Python, datos iniciales y script de despliegue para AWS Academy Learner Lab
docs/           Informe, prototipo, seguimiento y usuarios de prueba
CAMBIOS.md      Detalle de pantallas, capas y recursos de la app
```

## Cómo ejecutar

1. Abrir la carpeta del repositorio en Android Studio y esperar la sincronización de Gradle.
2. Ejecutar la configuración `app` en un emulador o dispositivo con Android 11 (API 30) o superior.
3. Ingresar con un usuario de prueba (ver [docs/usuarios-prueba.md](docs/usuarios-prueba.md)), por ejemplo `OP-4821` / `CruzDelSur2026`, o usar «Consultar mi equipaje» como pasajero.

El backend (DynamoDB, Lambda y API Gateway) se describe y se despliega según `aws-backend/README.md`. Las URL de las APIs se configuran en `app/src/main/java/com/grupo6/cruzdelsurapp/di/NetworkModule.kt`.

## Alcance funcional

La solución considera seis módulos:

| Módulo | Perfil principal | Función |
| --- | --- | --- |
| Autenticación | Operario | Inicio de sesión, recuperación de contraseña y cierre de sesión. |
| Registro de equipaje | Operario de counter | Registro de la unidad, generación del código único y vinculación con el boleto. |
| Control de ruta | Operario de bodega | Registro de carga, escala y entrega mediante lectura del código QR. |
| Consulta de equipaje | Pasajero | Consulta del estado y del historial a partir del código del contraste. |
| Manifiesto de bodega | Operario de bodega | Consulta de las unidades a bordo y de las que deben descargarse en cada escala. |
| Reporte de incidencias | Operario | Registro y seguimiento de una incidencia relacionada con una unidad. |

El entregable final se concentra en el flujo principal: autenticación del operario, registro de una unidad de equipaje, registro de eventos de control y consulta del estado por parte del pasajero. El manifiesto de bodega y el reporte de incidencias se encuentran representados en el prototipo y quedan documentados como ampliaciones del proyecto.

## Arquitectura propuesta

La aplicación se plantea con el patrón Modelo-Vista-ViewModel y una separación en tres capas:

- Presentación: pantallas desarrolladas con Jetpack Compose y Material Design 3.
- Dominio: ViewModel, estado de la interfaz y validaciones del flujo.
- Datos: repositorios y acceso a los servicios remotos.

Para la infraestructura en la nube se propone Amazon API Gateway como punto de acceso a los servicios REST, AWS Lambda para la lógica de cada operación y Amazon DynamoDB para almacenar las unidades de equipaje y sus eventos de control.

## Prototipo

El prototipo de alta fidelidad contiene las doce pantallas previstas para los perfiles de operario y pasajero.

[Abrir el prototipo en Google Stitch](https://stitch.withgoogle.com/projects/6488328065000050213)

Las capturas y la descripción de cada pantalla se encuentran en [docs/prototipo.md](docs/prototipo.md).

## Documentación

- [Informe del trabajo parcial](docs/informe-trabajo-parcial.md)
- [Prototipo y capturas](docs/prototipo.md)
- [Seguimiento del proyecto](docs/seguimiento.md)
- [Usuarios de prueba](docs/usuarios-prueba.md)

## Integrantes

- Salinas Cari, Jose
- Yllisca Chipana, Mario Renato
- Puma Jimenez, Josue Benjamin

## Información académica

- Curso: IS276, Plataformas Móviles y Análisis Cloud
- Docente: Jorge Rosvin Narváez Villacorta
- Semestre: 2026-2
- Institución: Universidad Peruana de Ciencias Aplicadas

## Repositorio

El repositorio contiene la documentación del proyecto, el código fuente de la aplicación Android y el backend en AWS. La rama `desarrollo` concentra el trabajo en curso y se integra en `main`.
