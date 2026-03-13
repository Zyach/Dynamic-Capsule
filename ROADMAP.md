# ROADMAP

Fecha de referencia: 2026-03-13
Fuente: `docs/INFORME_CONSULTORIA_2026-03-13.md`

## Objetivo

Llevar Dynamic-Capsule desde un prototipo funcional a un MVP robusto, seguro, medible y listo para releases repetibles.

## Horizonte 0: Estabilizacion inmediata

Meta: eliminar bloqueadores de seguridad, build y mantenibilidad.

- Endurecer broadcasts, receivers e intents internos.
- Definir pipeline GitHub Actions para Android.
- Anadir documentacion tecnica minima en `docs/`.
- Crear primera suite de tests reales para logica de negocio.
- Corregir permisos de `gradlew` y documentar setup Android SDK.

## Horizonte 1: MVP robusto

Meta: arquitectura sostenible y UX de activacion confiable.

- Refactor del sistema de plugins para una unica fuente de verdad.
- Reducir singletons y mover estado a capas testeables.
- Rehacer onboarding/permisos con enfoque de funnel UX.
- Limpiar dependencias legacy, huerfanas o desalineadas.
- Introducir observabilidad basica y manejo de errores consistente.

## Horizonte 2: Excelencia operativa

Meta: releases medibles y mejora continua automatizada.

- Automatizar build, firma, release y smoke test en dispositivo.
- Capturar logcat, `gfxinfo`, `meminfo` y tiempos de cold start.
- Anadir performance baselines y regression gates.
- Preparar checklist de cumplimiento para Play Store y privacidad.

## Horizonte 3: Expansion de producto

Meta: diferenciacion y crecimiento sostenible.

- Catalogo de plugins extensible.
- Nuevas experiencias contextuales: llamadas, timers, navegacion, eventos del sistema.
- Editor visual avanzado para isla/temas.
- Telemetria de producto para priorizar roadmap por uso real.

## Criterios de salida para MVP

- Build reproducible en CI y local.
- Sin riesgos criticos conocidos de IPC/permissions.
- Suite de tests minima pasando en cada PR.
- Onboarding con permisos claros y ratio de activacion medible.
- Release automatizado con smoke test basico.
