# Trabajo parcial

## Aplicación móvil para la trazabilidad del equipaje de bodega en la Empresa de Transportes Cruz del Sur S.A.

**Curso:** IS276, Plataformas Móviles y Análisis Cloud  
**Semestre:** 2026-2  
**Docente:** Jorge Rosvin Narváez Villacorta

**Integrantes:**

- Salinas Cari, Jose
- Yllisca Chipana, Mario Renato
- Puma Jimenez, Josue Benjamin

## 1. Introducción

El transporte terrestre interprovincial de pasajeros constituye un servicio fundamental para la conectividad del país. Según la Superintendencia de Transporte Terrestre de Personas, Carga y Mercancías (SUTRAN), durante el año 2024 los servicios de transporte terrestre de ámbito nacional movilizaron a 96 405 120 pasajeros. Cada viaje comprende una secuencia de procesos que va desde la compra del pasaje hasta la llegada del pasajero a su destino, y entre ellos se encuentra la gestión del equipaje de bodega, que inicia con su recepción por parte de la empresa transportista y continúa con la identificación, custodia, traslado y entrega en el punto de destino.

Ese proceso no está exento de riesgos. La pérdida, el deterioro o la sustracción del equipaje generan reclamos que la empresa debe atender, y el Instituto Nacional de Defensa de la Competencia y de la Protección de la Propiedad Intelectual (Indecopi) establece que la responsabilidad por los daños ocasionados recae en la empresa de transporte. Cuando el registro del equipaje se sustenta únicamente en un contraste físico en papel, la organización no dispone de evidencia digital del recorrido de cada unidad y la atención de un reclamo depende de la reconstrucción manual de lo ocurrido.

El presente trabajo propone el desarrollo de una aplicación móvil Android que digitalice el registro y la consulta de la trazabilidad del equipaje de bodega en la Empresa de Transportes Cruz del Sur S.A. La aplicación se construye sobre una arquitectura dividida en capas con acceso a servicios en la nube, conforme al logro del curso, y cubre dos perfiles de uso: el operario de terminal y bodega, que registra los eventos de control en cada punto de la ruta, y el pasajero, que consulta el estado de su equipaje.

Es necesario precisar el alcance. El trabajo no afirma que la organización presente una incidencia elevada de pérdida de equipaje, dado que no se dispone de información pública que permita sustentarlo. El objeto de estudio es el proceso de gestión y trazabilidad del equipaje de bodega, desde su recepción hasta su entrega, con la finalidad de identificar las deficiencias del diseño de dicho proceso y proponer una solución móvil que mejore su control.

## 2. Objetivos

### 2.1 Objetivo general

Desarrollar una aplicación móvil Android que permita registrar y consultar la trazabilidad del equipaje de bodega de la Empresa de Transportes Cruz del Sur S.A. en los puntos de control de la ruta, mediante una arquitectura dividida en capas con acceso a servicios en la nube, al término del semestre académico 2026-2.

### 2.2 Objetivos específicos

**OE1.** Diseñar el prototipo de alta fidelidad de la aplicación, cubriendo los flujos de autenticación, registro de equipaje, control de puntos de ruta y consulta del pasajero.

**OE2.** Implementar la interfaz de usuario con Jetpack Compose, aplicando los lineamientos de Material Design 3 y componentes reutilizables.

**OE3.** Estructurar la aplicación bajo el patrón Modelo-Vista-ViewModel, separando las capas de presentación, dominio y datos.

**OE4.** Integrar la aplicación con servicios en la nube de Amazon Web Services para la persistencia y consulta de los eventos de trazabilidad.

**OE5.** Validar el funcionamiento del flujo principal de la aplicación mediante pruebas de ejecución en emulador y la obtención de métricas de uso.

## 3. Descripción de la empresa

Cruz del Sur es una empresa peruana dedicada al transporte terrestre interprovincial de pasajeros y a otros servicios relacionados con el transporte. Fue fundada en 1960 en la localidad de La Punta de Bombón, en el departamento de Arequipa, y a lo largo de los años amplió sus operaciones hasta contar con rutas que conectan diversos destinos nacionales e internacionales. Su presencia en el sector del transporte interprovincial motivó su selección como organización objeto de estudio.

### 3.1 Misión

Crear experiencia de viaje y servicios únicos en el transporte de pasajeros y carga a través de una propuesta de valor innovadora, confiable, eficiente y de calidad.

### 3.2 Visión

Ser el referente en el transporte de pasajeros y de carga en el Perú y Sudamérica.

### 3.3 Proceso en estudio

La organización opera diversos procesos de negocio. El presente trabajo se centra exclusivamente en el proceso de gestión y control del equipaje de bodega, que comprende cuatro etapas sucesivas y constituye el ámbito donde se sitúa el problema que la aplicación busca resolver.

| N.° | Etapa | Descripción | Responsable |
| --- | --- | --- | --- |
| 1 | Recepción | El pasajero entrega su equipaje en el counter del terminal de origen y recibe un contraste físico. | Personal de counter |
| 2 | Carga | El equipaje se traslada a la bodega del bus y se acomoda según el destino. | Personal de bodega |
| 3 | Escala | En los terminales intermedios se descarga y recarga el equipaje según los destinos. | Personal de bodega |
| 4 | Entrega | El equipaje se entrega al pasajero en el terminal de destino contra la presentación del contraste. | Personal de bodega |

### 3.4 Política de calidad

La Política de Calidad de la organización establece, como primer lineamiento, satisfacer las necesidades y expectativas de los clientes en todos los puntos de contacto, enfocando los esfuerzos en la calidad del servicio, la comodidad, la seguridad y la puntualidad. La recepción, custodia y entrega del equipaje de bodega constituyen precisamente puntos de contacto de esa naturaleza, de modo que mejorar su trazabilidad contribuye de manera directa al cumplimiento de dicho lineamiento.

## 4. Descripción de la problemática

El proceso de gestión del equipaje de bodega se sustenta en un contraste físico en papel que se entrega al pasajero en el momento de la recepción y que se exige a la hora de la entrega. Ese contraste cumple una función de identificación en los extremos del proceso, pero no deja registro alguno de lo ocurrido entre ambos momentos. En consecuencia, las etapas intermedias de carga y escala no producen evidencia verificable, y el proceso no permite reconstruir el recorrido de una unidad de equipaje ni determinar en qué punto se produjo una incidencia.

De esta condición se derivan tres consecuencias sobre la operación:

- El personal de bodega no dispone de información sobre qué unidades corresponden a cada destino en una escala, lo que obliga a una verificación visual y aumenta la probabilidad de error en la descarga.
- El pasajero no tiene forma de conocer el estado de su equipaje durante el viaje y, ante cualquier duda, debe recurrir al personal del terminal.
- La atención de un reclamo depende de la reconstrucción manual de los hechos, lo que extiende el tiempo de respuesta y debilita la posición de la empresa frente a la responsabilidad que le atribuye la normativa.

El problema central se formula en los siguientes términos: el proceso de gestión del equipaje de bodega no genera registro digital verificable de los eventos de control, lo que impide determinar la ubicación y el estado de cada unidad durante el viaje.

## 5. Propuesta de solución

Se propone el desarrollo de una aplicación móvil Android que digitalice el registro de los eventos de control del equipaje y ponga esa información a disposición tanto del personal operativo como del pasajero. Cada unidad de equipaje recibe un código único vinculado al boleto, materializado en una etiqueta con código QR, y cada paso por un punto de control genera un evento registrado desde la aplicación.

### 5.1 Alcance funcional

| Módulo | Perfil | Funcionalidad | Prioridad |
| --- | --- | --- | --- |
| Autenticación | Operario | Inicio de sesión, recuperación de contraseña y cierre de sesión. | Alta |
| Registro de equipaje | Operario de counter | Registro de la unidad, generación del código único y vinculación al boleto. | Alta |
| Control de ruta | Operario de bodega | Registro de los eventos de carga, escala y entrega mediante lectura del código. | Alta |
| Consulta de equipaje | Pasajero | Consulta del estado y del historial de eventos a partir del código del contraste. | Alta |
| Manifiesto de bodega | Operario de bodega | Listado de las unidades a bordo y de las que corresponde descargar en cada escala. | Media |
| Reporte de incidencias | Operario | Registro de una incidencia sobre una unidad y su estado de atención. | Media |

La prioridad determina qué módulos se implementan en el entregable final y cuáles se representan únicamente en el prototipo.

### 5.2 Arquitectura de la solución

La aplicación se estructura bajo el patrón Modelo-Vista-ViewModel, con una separación en tres capas. La capa de presentación se construye con Jetpack Compose y Material Design 3, y comprende las pantallas y los componentes reutilizables; no contiene lógica de negocio. La capa de dominio aloja los ViewModel, que exponen el estado de la interfaz y concentran las validaciones. La capa de datos implementa el patrón Repository y resuelve el acceso a los servicios remotos.

La persistencia y la lógica del lado del servidor se resuelven con servicios de Amazon Web Services. Amazon API Gateway expone los servicios REST que consume la aplicación, AWS Lambda implementa la lógica de cada operación y Amazon DynamoDB almacena las unidades de equipaje y sus eventos de control.

### 5.3 Alcance de la implementación

El prototipo se presenta completo, con la totalidad de las pantallas de los seis módulos. La implementación en código, en cambio, se delimita al flujo principal del proceso: autenticación del operario, registro de una unidad de equipaje, registro de los eventos de control en la ruta y consulta del estado por parte del pasajero. Los módulos de prioridad media se representan en el prototipo y quedan documentados como trabajo futuro.

## 6. Planificación

La planificación y el seguimiento de las tareas del equipo se gestionan en un tablero digital, sobre el cual se registran las actividades, sus responsables y su estado de avance. Las evidencias de dicho seguimiento se presentan en [seguimiento.md](seguimiento.md).

El cronograma académico consignado en el informe parcial es el siguiente:

| Semana | Actividad | Entregable | Responsable |
| --- | --- | --- | --- |
| 4 | Definición del problema, elaboración del prototipo y del informe de avance. | Trabajo parcial | Equipo |
| 5 | Implementación de la interfaz en Jetpack Compose y de los componentes reutilizables. | Módulos de interfaz | Por asignar |
| 5 | Estructuración de la aplicación bajo el patrón MVVM y definición del modelo de datos. | Capas de la aplicación | Por asignar |
| 6 | Configuración de los servicios en AWS e implementación de los servicios REST. | Servicios en la nube | Por asignar |
| 6 | Integración de la aplicación con los servicios remotos. | Aplicación integrada | Por asignar |
| 7 | Pruebas del flujo principal, obtención de métricas y elaboración del informe final. | Trabajo final | Equipo |

## 7. Historias de usuario

Las historias de usuario se redactan desde la perspectiva de los dos perfiles identificados y constituyen la base para la definición de las pantallas del prototipo y para la implementación posterior.

| Código | Historia de usuario | Criterios de aceptación | Prioridad |
| --- | --- | --- | --- |
| HU01 | Como operario, quiero iniciar sesión con mi usuario y contraseña para acceder a las funciones que corresponden a mi rol. | El sistema valida las credenciales, muestra un mensaje de error claro cuando son incorrectas y mantiene la sesión activa. | Alta |
| HU02 | Como operario, quiero recuperar mi contraseña para recobrar el acceso cuando la haya olvidado. | El sistema solicita el correo institucional y confirma el envío de las instrucciones de recuperación. | Media |
| HU03 | Como operario de counter, quiero registrar una unidad de equipaje y vincularla al boleto del pasajero para generar su código único. | El sistema genera un código único, lo asocia al boleto y muestra la etiqueta con el código QR para su impresión. | Alta |
| HU04 | Como operario de bodega, quiero registrar la carga de una unidad leyendo su código para dejar constancia de su ingreso al bus. | El sistema registra el evento con la fecha, la hora y el terminal, y rechaza un código que no corresponda al viaje. | Alta |
| HU05 | Como operario de bodega, quiero consultar el manifiesto de las unidades a bordo para saber cuáles descargar en cada escala. | El sistema lista las unidades agrupadas por destino e identifica las que corresponden a la escala actual. | Media |
| HU06 | Como operario de bodega, quiero registrar la entrega de una unidad al pasajero para cerrar su trazabilidad. | El sistema registra el evento de entrega, cambia el estado de la unidad a entregada e impide un segundo registro. | Alta |
| HU07 | Como pasajero, quiero consultar el estado de mi equipaje con el código de mi contraste para conocer su ubicación durante el viaje. | El sistema muestra el estado actual y el historial de eventos con la fecha, la hora y el lugar de cada uno. | Alta |
| HU08 | Como operario, quiero registrar una incidencia sobre una unidad de equipaje para dejar constancia de una situación irregular. | El sistema permite seleccionar el tipo de incidencia, adjuntar una descripción y consultar su estado de atención. | Media |
| HU09 | Como operario, quiero ver el detalle de una unidad de equipaje para verificar sus datos y su recorrido completo. | El sistema muestra el código, el boleto asociado, el destino, el estado y la secuencia de eventos registrados. | Alta |
| HU10 | Como operario, quiero cerrar mi sesión para impedir el uso de la aplicación por parte de terceros. | El sistema finaliza la sesión, retorna a la pantalla de inicio y exige nuevas credenciales para reingresar. | Media |

## 8. Repositorio de código fuente

El código fuente y la documentación de la aplicación se alojan en el siguiente repositorio:

[github.com/redneurocom/cruzdelsurapp](https://github.com/redneurocom/cruzdelsurapp)

El repositorio se encuentra creado para el presente avance. La carga del código fuente corresponde al entregable final, conforme al alcance definido para cada entrega.

## 9. Prototipo

El prototipo se elaboró en alta fidelidad y comprende la totalidad de las pantallas de la aplicación, incluidos los módulos cuya implementación en código queda fuera del alcance del entregable final. El diseño aplica los lineamientos de Material Design 3 y respeta los principios de usabilidad revisados en el curso, en particular la visibilidad del estado del sistema, la prevención de errores y la consistencia de los componentes.

[Abrir el prototipo en Google Stitch](https://stitch.withgoogle.com/projects/6488328065000050213)

Las doce pantallas y sus descripciones se encuentran en [prototipo.md](prototipo.md).

## 10. Anexos

- [Evidencia del seguimiento de tareas](seguimiento.md)
- [Capturas del prototipo](prototipo.md)
