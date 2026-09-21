# Cruz del Sur: trazabilidad de equipaje de bodega

Este repositorio reúne el avance del proyecto desarrollado para el curso IS276, Plataformas Móviles y Análisis Cloud, de la Universidad Peruana de Ciencias Aplicadas.

La propuesta consiste en una aplicación móvil Android para registrar y consultar la trazabilidad del equipaje de bodega en la Empresa de Transportes Cruz del Sur S.A. Cada unidad de equipaje se identifica mediante un código único y una etiqueta QR. A partir de esa identificación, el personal puede registrar los eventos de recepción, carga, escala y entrega, mientras que el pasajero puede consultar el estado y el historial de su equipaje.

## Estado actual

El proyecto se encuentra en la etapa de trabajo parcial. Hasta el momento se han completado las siguientes actividades:

- Delimitación del problema y del proceso de equipaje de bodega.
- Definición del objetivo general, los objetivos específicos y el alcance.
- Elaboración de diez historias de usuario con sus criterios de aceptación.
- Definición de la arquitectura propuesta con MVVM y servicios de AWS.
- Diseño del prototipo de alta fidelidad con doce pantallas.
- Organización del seguimiento de actividades en un tablero Kanban.
- Elaboración y revisión del informe del trabajo parcial.

La implementación en Android, la integración con los servicios en la nube y las pruebas corresponden a las siguientes etapas del curso. Por esa razón, este repositorio contiene por ahora la documentación del proyecto y las evidencias del prototipo, sin presentar como terminadas funciones que todavía no han sido desarrolladas.

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

El repositorio se utilizará para mantener la documentación, gestionar el control de versiones y añadir progresivamente el código fuente de la aplicación durante las siguientes etapas del proyecto.
