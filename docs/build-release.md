# Build y Release

Fecha de referencia: 2026-03-13

## Requisitos locales

- JDK 17 o superior.
- Android SDK con:
  - `platform-tools`
  - `platforms;android-36`
  - `build-tools;36.0.0`

## Setup local

1. Copia `local.properties.example` como `local.properties`.
2. Ajusta `sdk.dir` al path de tu Android SDK.
3. Asegura permisos del wrapper:

```bash
chmod +x gradlew
```

## Comandos principales

```bash
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleRelease
```

## CI actual

El workflow `android-ci.yml` ejecuta:

- `lintDebug`
- `testDebugUnitTest`
- `assembleRelease`

Y publica artefactos de reportes y outputs de build.

## Criterio operativo

- La compilacion y validacion oficiales del proyecto ocurren en GitHub Actions.
- El disparador esperado es `commit + push` de cambios relevantes del codigo.
- La validacion local es auxiliar y no reemplaza el resultado del workflow remoto.

## Siguiente fase recomendada

- Agregar firma de release segura via secrets.
- Versionado automatico basado en tags.
- Job posterior de smoke test en dispositivo/emulador.
- Recoleccion automatica de `logcat`, `gfxinfo` y `meminfo`.
