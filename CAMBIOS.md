# Cruz del Sur — Trazabilidad de equipaje de bodega (Grupo 6)

App Android (Kotlin + Jetpack Compose) construida sobre la arquitectura del proyecto base del curso: **MVVM + casos de uso + repositorios + Hilt + Retrofit**. Las pantallas siguen el prototipo de alta fidelidad de Cruz del Sur.

## Identificación
| Elemento | Valor |
| --- | --- |
| `applicationId` | `appmovile.grupo6` |
| Paquete / `namespace` | `com.grupo6.cruzdelsurapp` |
| Proyecto Gradle | `appmovile-grupo6` |
| Nombre visible | Cruz del Sur |
| Tema | `Theme.CruzDelSurApp` / `CruzDelSurAppTheme` |
| minSdk / targetSdk | 30 / 37 |

## Pantallas
| Ruta | Pantalla (prototipo) | Quién |
| --- | --- | --- |
| `login` | Inicio de sesión (1) | Operario; enlace «Consultar mi equipaje» para el pasajero |
| `inicio` | Manifiesto de bodega (8) | Operario |
| `form?id={id}` | Registro / edición de equipaje (4) | Operario |
| `detalle/{id}` | Detalle de la unidad (9) | Operario (con editar/eliminar) y pasajero (solo lectura) |
| `buscar` | Consulta del pasajero (11) | Todos; busca por pasajero, boleto o código `CDS-…` |

## Capas
- **Dominio** (`domain`): modelos `Equipaje`, `Destino`, `Usuario`; repositorios `EquipajeRepository`, `AuthRepository`; casos de uso `GetEquipajesUseCase`, `GetEquipajeUseCase`, `RegistrarEquipajeUseCase`, `ActualizarEquipajeUseCase`, `EliminarEquipajeUseCase`, `GetDestinosUseCase`, `IniciarSesionUseCase`.
- **Datos** (`data`): `EquipajeApiService` (`/equipajes`), `DestinoApiService` (`/destinos`), `AuthApiService` (`/auth/login`), sus DTO, `EquipajeRepositoryImpl` y `AuthRepositoryImpl`.
- **DI** (`di`): `NetworkModule` con un Retrofit por API (`EQUIPAJES_BASE_URL`, `DESTINOS_BASE_URL`, `AUTH_BASE_URL`) y un interceptor que agrega `Authorization: Bearer <token>` cuando hay sesión.
- **Presentación** (`presentation`): pantallas con su `UiState` y `ViewModel`, componentes comunes (`EquipajeItem`, `SeccionCard`, `EstadoChip`, barras superior e inferior), `UiEventBus` para los mensajes y `SesionManager` para la sesión del operario (en memoria).
- **Tema** (`ui/theme`): paleta Cruz del Sur (azul marino, rojo de marca y estados verde, rojo y naranja) y tipografía.

## Backend
Carpeta `aws-backend` (ver su `README.md`): 3 tablas DynamoDB, 3 Lambdas en Python (`funEquipajes`, `funDestinos`, `funAuth`) y 3 APIs HTTP (`apiEquipajes`, `apiDestinos`, `apiAuth`) en AWS Academy Learner Lab.

## Pruebas
- `app/src/test/.../PresentacionUtilidadesTest.kt`: código de rastreo, iniciales, nombres de rol y franquicia (JVM).
- `app/src/androidTest/.../EquipajeItemTest.kt`: tarjeta del manifiesto e identificador de la app (emulador).

## Recursos
- `res/drawable`: `logo_cds.xml`, `ilustracion_bus.xml` y el ícono adaptable (`ic_launcher_background.xml`, `ic_launcher_foreground.xml`).
- `res/mipmap-anydpi`: ícono de la app (Android 8+, cubre el minSdk 30).
- `res/values`: `strings.xml` (`app_name`), `themes.xml` y `colors.xml` (fondo de la ventana).
- `res/xml`: reglas de copia de seguridad referenciadas en el manifiesto.
