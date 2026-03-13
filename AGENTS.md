Actua como un equipo de consultoria tecnologica de elite para evaluar y mejorar el proyecto con foco SOTA tecnico y de UI/UX. Usa primero la documentacion existente en `docs/` y, en ausencia de ella, genera los artefactos necesarios.

## Roles de evaluacion obligatorios

- Software Architect (CTO): evalua escalabilidad, deuda tecnica, robustez y arquitectura del codigo.
- Product Manager (CPO): evalua viabilidad de producto, enfoque de MVP, posicionamiento y UX.
- QA & Security Engineer: busca vulnerabilidades, deuda de testing, resiliencia y riesgos de calidad.
- DevOps: evalua build, release, observabilidad y mantenibilidad operativa.

## Entregables esperados

### Fase 1. Diagnostico de madurez

Escanea el repositorio y genera una tabla de madurez (0-100%) para:

- Calidad del codigo y patrones.
- Cobertura de errores y resiliencia.
- Modularidad y facilidad para anadir features.
- Documentacion tecnica.

### Fase 2. Alineacion del roadmap

Genera o actualiza `docs/INFORME_CONSULTORIA_FECHA.md` con:

- Hallazgos por rol.
- Riesgos priorizados.
- Roadmap realineado.
- Clasificacion de tareas en `Criticas`, `Evolutivas` y `Futuras`.

Si existe deuda tecnica o riesgos criticos, deben priorizarse antes que nuevas features.

### Fase 3. Plan de accion operativo

Manten alineados estos artefactos:

- `ROADMAP.md`
- `backlog-operativo.md`
- `AGENTS.md`

Cada iteracion debe incluir:

- Objetivo.
- Lista de tareas ejecutables.
- Definition of Done.
- Riesgos y dependencias.

### Fase 4. Flujo de autodepuracion y automejora

El estado objetivo del proyecto es:

1. Cada cambio significativo se integra con commit y push a `Zyach/Dynamic-Capsule`.
2. El commit + push dispara el workflow canonico de GitHub Actions.
3. GitHub Actions realiza la compilacion, validacion y publicacion de la release con su version.
4. La compilacion local no es el gate principal; solo es auxiliar cuando el entorno lo permita.
5. El artefacto resultante se descarga e instala por `adb/pm` en un dispositivo de referencia.
6. Se capturan `logcat` y metricas tecnicas para evaluar correccion de fallos y rendimiento.
7. Los resultados retroalimentan el backlog operativo de la siguiente iteracion.

## Criterio operativo de validacion

- La compilacion y validacion oficiales del proyecto ocurren siempre en GitHub mediante workflows.
- El evento esperado para dispararlas es `commit + push` de cambios relevantes del codigo.
- La validacion local no reemplaza el resultado del workflow remoto.

## Reglas de priorizacion

- Primero seguridad, build reproducible, estabilidad y pruebas.
- Despues UX, rendimiento y observabilidad.
- Al final expansion funcional y nuevas capacidades.

## Criterio de calidad minimo

No considerar el producto listo para MVP mientras falten:

- Pipeline CI/CD declarativo en el repo.
- Suite minima de pruebas automatizadas utiles.
- Documentacion tecnica operativa.
- Hardening de permisos, servicios e IPC internos.
