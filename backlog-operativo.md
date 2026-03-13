# Backlog Operativo

Fecha de referencia: 2026-03-13

## Iteracion 1

Objetivo: cerrar riesgos criticos y habilitar base de trabajo estable.

- Reemplazar broadcasts implicitos/exportados por un mecanismo seguro y documentado.
- Corregir arquitectura de inicializacion de plugins para evitar instancias duplicadas.
- Crear `docs/arquitectura.md`, `docs/build-release.md` y `docs/permisos-y-privacidad.md`.
- Anadir `local.properties.example` o documentacion equivalente para Android SDK.
- Crear GitHub Actions con jobs de `unit-test`, `lint` y `assemble-release`.
- Implementar tests unitarios para `IslandSettings` y reglas de prioridad de `PluginManager`.

Definition of Done:

- Riesgos criticos de IPC reducidos con cambio verificable en codigo.
- CI ejecutandose desde el repo.
- Al menos 5 pruebas utiles pasando.
- Documentacion tecnica base disponible.

## Iteracion 2

Objetivo: refactor sostenible y mejora de UX de activacion.

- Introducir capas `core/domain/platform/feature` o similar sin sobreingenieria.
- Encapsular estado global y eliminar dependencias estaticas evitables.
- Revisar `HomeScreen` para un onboarding mas guiado, medible y menos ambiguo.
- Alinear versiones Compose/testing y eliminar Firebase si sigue sin uso real.
- Anadir estados de error/permiso revocado en UI.

Definition of Done:

- Modulos o capas con responsabilidades mas limpias.
- Menor uso de singletons mutables.
- Onboarding revisado con criterios UX claros.

## Iteracion 3

Objetivo: observabilidad y calidad de release.

- Agregar crash reporting/telemetria opt-in.
- Anadir smoke tests instrumentados para flujo principal.
- Automatizar recoleccion de `logcat`, `gfxinfo` y `meminfo` tras release.
- Definir tablero de salud: crashes, ANR, startup time, consumo memoria.

Definition of Done:

- Cada release produce artefacto y reporte tecnico.
- Hay metricas comparables entre versiones.

## Cola futura priorizada

- Plugin framework extensible con contrato estable.
- Nuevos plugins contextuales.
- Editor visual avanzado de isla y temas.
- Experimentos UX guiados por telemetria.
