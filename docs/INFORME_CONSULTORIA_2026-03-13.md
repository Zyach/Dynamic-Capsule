# Informe de Consultoria Tecnologica

Fecha: 2026-03-13
Proyecto: Dynamic-Capsule
Alcance: auditoria tecnica del codigo Android, UX/UI, QA, seguridad, operacion y alineacion de roadmap/backlog.

## Resumen ejecutivo

Dynamic-Capsule tiene una base funcional interesante para un MVP de Dynamic Island en Android 12+, con una UI en Jetpack Compose y una idea de producto clara. Sin embargo, el estado actual esta mas cerca de un prototipo avanzado que de un producto listo para escalar o liberar con confianza.

Los principales bloqueadores son:

- Seguridad de IPC/broadcasts demasiado abierta para acciones sensibles.
- Arquitectura con singletons y estado global acoplado a servicios privilegiados.
- Casi ausencia total de pruebas automatizadas utiles.
- Falta de pipeline CI/CD declarativo en el repo y falta de documentacion tecnica operativa.
- Mezcla de dependencias/versiones y librerias no utilizadas o envejecidas.

## Metodologia

Se revisaron los siguientes activos principales:

- `README.md`, `build.gradle`, `app/build.gradle`, `AGENTS.md`.
- Manifest y servicios Android privilegiados.
- Sistema de plugins, settings globales, navegacion y pantallas base Compose.
- Tests presentes en `app/src/test` y `app/src/androidTest`.
- Estructura del repo y contenido de `.github/`.

## Diagnostico por rol

### Software Architect (CTO)

Fortalezas:

- Stack moderno base: Kotlin + Jetpack Compose + Navigation Compose.
- Separacion funcional inicial por plugins (`notification`, `media`, `battery`).
- El producto ya encapsula una capacidad diferenciadora: overlay tipo Dynamic Island.

Debilidades:

- Estado compartido via singletons globales (`IslandSettings.instance`, `Theme.instance`, referencias estaticas a servicios), lo que dificulta testing, modularidad y evolucion.
- `PluginManager` crea instancias concretas internamente y no trabaja con inversion de dependencias, registro desacoplado ni ciclo de vida seguro.
- `IslandOverlayService` administra Compose y recomposer manualmente, con alto riesgo de fugas, estados zombis y complejidad accidental.
- El boundary entre UI, dominio y servicios Android esta poco definido; varias decisiones de negocio viven dentro de servicios y composables.

### Product Manager (CPO)

Fortalezas:

- Propuesta de valor clara para usuarios Android que buscan una experiencia inspirada en Dynamic Island.
- Ajustes de tema, posicion, tamano y apps habilitadas aportan personalizacion real.

Riesgos de producto y UX:

- El onboarding depende de permisos sensibles y complejos, pero la experiencia de activacion aun no parece guiada por un funnel medible.
- No hay evidencia de analitica, telemetria, experimentacion ni investigacion UX documentada.
- No existe una narrativa de roadmap centrada en MVP estable; el repo transmite mas exploracion que estrategia de producto.

### QA & Security Engineer

Hallazgos principales:

- Los receivers usan `RECEIVER_EXPORTED` y las acciones se envian por `sendBroadcast` con intents implicitos para operaciones sensibles; esto abre la puerta a spoofing/interferencia por apps externas.
- El proyecto pide `QUERY_ALL_PACKAGES`, permiso de alto escrutinio en Play Store, sin documentacion de mitigacion/compliance.
- Los tests utiles son inexistentes; solo hay plantillas de ejemplo.
- No hay hardening visible para errores de intents, permisos revocados, nulos de servicios ni reconexion robusta.

### DevOps

Fortalezas:

- Proyecto Gradle relativamente simple y facil de entender.

Debilidades:

- No existen workflows en `.github/workflows` pese a que el contrato operativo los asume.
- El repo no incluye documentacion de build reproducible, ni `local.properties.example`, ni instrucciones CI para Android SDK.
- La validacion `bash gradlew test` falla en este entorno por falta de SDK configurado, lo que confirma que el proyecto no esta preparado para una ejecucion portable fuera del setup local.

## Tabla de madurez

| Eje | Madurez | Diagnostico |
|---|---:|---|
| Calidad del codigo y patrones | 58% | Hay base moderna en Compose/Kotlin, pero con acoplamiento alto, estado global y gestion manual compleja de servicios/overlay. |
| Cobertura de errores y resiliencia | 27% | Faltan pruebas, manejo robusto de fallos y endurecimiento de permisos, receivers, intents y ciclo de vida. |
| Modularidad (facilidad para anadir features) | 49% | El concepto de plugins ayuda, pero hoy esta fuertemente acoplado a implementaciones concretas y servicios singleton. |
| Documentacion tecnica | 22% | Solo existe README general; faltan docs de arquitectura, CI/CD, testing, release y operacion. |

## Hallazgos tecnicos prioritarios

### Criticos

1. Superficie de ataque en broadcasts internos.
   - Referencias: `app/src/main/java/fr/angel/dynamicisland/model/service/NotificationService.kt`, `app/src/main/java/fr/angel/dynamicisland/model/service/IslandOverlayService.kt`, `app/src/main/java/fr/angel/dynamicisland/plugins/notification/NotificationPlugin.kt`, `app/src/main/java/fr/angel/dynamicisland/model/Constants.kt`.
   - Riesgo: apps externas pueden potencialmente emitir acciones internas como cierre/apertura o eventos, o escuchar/reinyectar flujos si el modelo IPC no se restringe.

2. Cobertura de testing practicamente nula.
   - Referencias: `app/src/test/java/fr/angel/dynamicisland/ExampleUnitTest.kt`, `app/src/androidTest/java/fr/angel/dynamicisland/ExampleInstrumentedTest.kt`.
   - Riesgo: cualquier regresion en plugins, settings, permisos o servicios llegara a produccion sin red de seguridad.

3. Ausencia de CI/CD declarativo en el repo.
   - Referencia: `.github/` sin `.github/workflows/`.
   - Riesgo: el flujo de release descrito en AGENTS no es auditable ni reproducible desde repositorio.

4. Gestion manual y fragil del overlay Compose en servicio privilegiado.
   - Referencia: `app/src/main/java/fr/angel/dynamicisland/model/service/IslandOverlayService.kt`.
   - Riesgo: fugas de memoria, recomposicion fuera de ciclo de vida esperado y bugs dificiles de reproducir.

### Altas

1. Dependencias mezcladas o desalineadas.
   - Referencia: `app/build.gradle`.
   - Observacion: BOM Compose 2026 coexistiendo con artefactos pinneados, test BOM de 2023, Accompanist antiguo y Firebase incluido sin uso aparente.

2. Permisos sensibles con riesgo de cumplimiento de tienda.
   - Referencia: `app/src/main/AndroidManifest.xml`.
   - Observacion: `QUERY_ALL_PACKAGES`, overlay y accesibilidad requieren justificacion documental y tecnica fuerte.

3. Roadmap inexistente en el repo.
   - Impacto: el backlog no esta conectado con criterios de salida MVP, telemetria ni releases.

### Medias

1. UX de onboarding valida pero no instrumentada.
   - Referencia: `app/src/main/java/fr/angel/dynamicisland/ui/home/HomeScreen.kt`.

2. `PluginManager` y `ExportedPlugins` duplican conocimiento y crean instancias separadas.
   - Referencias: `app/src/main/java/fr/angel/dynamicisland/plugins/PluginManager.kt`, `app/src/main/java/fr/angel/dynamicisland/plugins/ExportedPlugins.kt`.

3. El script de build no es ejecutable por permisos de archivo en este clon.
   - Referencia: `gradlew` sin bit ejecutable.

## Validacion operativa realizada

Se intento ejecutar validacion basica con:

```bash
bash gradlew test
```

Resultado:

- Fallo por falta de Android SDK configurado (`ANDROID_HOME` o `local.properties`).
- Conclusion: hoy el proyecto no ofrece una experiencia de build reproducible lista para CI o para nuevos colaboradores.

## Roadmap realineado

### Tareas criticas

- Encapsular todos los broadcasts internos con mecanismo seguro: receiver no exportado, intents explicitos, permiso custom signature o migracion a canal interno seguro.
- Introducir una capa de arquitectura minima: `core`, `domain`, `platform`, `feature-*` o equivalente ligero, separando servicios Android de logica de negocio.
- Reescribir la inicializacion y el registro de plugins para que exista una sola fuente de verdad y se pueda testear.
- Crear suite minima de tests de dominio y de integracion para settings, prioridad de plugins, filtros de notificaciones y permisos.
- Anadir CI GitHub Actions para build, unit tests, lint, assemble release y artefactos.
- Documentar arquitectura, release process, permisos y politica de privacidad tecnica.

### Tareas evolutivas

- Redisenar onboarding y settings con focus en funnel de activacion y claridad de permisos.
- Sustituir dependencias legacy/no usadas y alinear versiones de Compose, testing y accompanist.
- Anadir logging estructurado, observabilidad basica y crash reporting opt-in.
- Mejorar resiliencia del overlay ante rotacion, lockscreen, muerte del proceso y revocacion de permisos.
- Disenar baseline performance profile y mediciones de arranque/render.

### Tareas futuras

- Sistema de plugins extensible con contrato estable y catalogo dinamico.
- Smart summaries de notificaciones, reply mejorado y acciones contextuales.
- Integraciones avanzadas: llamadas, temporizadores, navegacion, eventos del sistema.
- Sistema de temas premium o editor visual de island.

## Recomendaciones SOTA

- Adoptar arquitectura limpia pragmatica, no ceremoniosa: repositorios solo donde agreguen valor, casos de uso para reglas criticas, `ViewModel` para pantallas y servicios desacoplados.
- Reemplazar singleton mutable por DI ligera (`Hilt` o `Koin`; si se quiere bajo costo, locator interno bien encapsulado como paso transitorio).
- Definir un `design system` Compose pequeno pero consistente para cards, estados vacios, permisos y feedback.
- Medir UX real con eventos de onboarding: permiso overlay, permiso accesibilidad, plugin activado, primer evento visible, churn de setup.

## Propuesta de iteraciones

### Iteracion 1

- Cerrar riesgos criticos de IPC/broadcast.
- Crear pipeline CI minimo.
- Escribir docs tecnicas base.
- Anadir tests de settings y `PluginManager`.

### Iteracion 2

- Refactor de arquitectura de plugins y estado.
- Rehacer onboarding y permisos con UX guiada.
- Alinear dependencias y limpiar librerias no usadas.

### Iteracion 3

- Telemetria, crash reporting, performance baselines.
- Suite de instrumentacion para permisos y flujos principales.
- Preparacion release candidate del MVP.

## Flujo de autodepuracion y automejora recomendado

Estado actual: no es ejecutable aun desde este repo porque faltan workflows, SDK/entorno reproducible y documentacion del pipeline.

Flujo objetivo por cada cambio significativo:

1. Commit semantico y push a `main` o rama release.
2. GitHub Actions ejecuta `lint`, `testDebugUnitTest`, `assembleRelease` y firma artefacto.
3. Workflow publica release con version calculada desde tags o version catalog.
4. Job posterior descarga APK/AAB, instala via `adb install -r` en dispositivo de referencia.
5. Se capturan `logcat`, `dumpsys gfxinfo`, `meminfo`, tiempos de arranque y errores del servicio.
6. Se adjunta reporte automatico al release o al PR.

## Conclusion

El producto tiene potencial alto, pero la prioridad no debe ser anadir nuevas features todavia. La prioridad correcta es endurecer seguridad, estabilizar arquitectura, habilitar pruebas/CI y profesionalizar documentacion y operacion. Una vez cubierto eso, el proyecto puede evolucionar rapidamente hacia una base SOTA tanto tecnica como de UX.
