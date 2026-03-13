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

El workflow `android-release.yml` ejecuta:

- `lintDebug`
- `testDebugUnitTest`
- `assembleRelease`
- publicacion de un GitHub Release con el APK adjunto

En `push` a `main` o `master`, el flujo publica un release en GitHub en lugar de dejar solo artifacts efimeros.

## Versionado incremental

- `baseVersionCode` y `baseVersionName` viven en `app/build.gradle`.
- En GitHub Actions se calcula un `VERSION_CODE` incremental usando `baseVersionCode + github.run_number`.
- En GitHub Actions se calcula un `VERSION_NAME` incremental con formato `baseVersionName+run_number`.
- Esto permite que cada APK publicado tenga un `versionCode` mayor que el anterior y pueda actualizarse sobre instalaciones previas.

## Firma para upgrades

- Para que Android permita instalar una version encima de otra, los APK deben estar firmados con la misma clave.
- El workflow soporta firma desde GitHub Secrets mediante:
  - `ANDROID_KEYSTORE_BASE64`
  - `ANDROID_KEYSTORE_PASSWORD`
  - `ANDROID_KEY_ALIAS`
  - `ANDROID_KEY_PASSWORD`
- Si esos secrets no existen, el workflow seguira compilando pero el APK de release no tendra garantizada compatibilidad de actualizacion entre versiones instaladas.

## Criterio operativo

- La compilacion y validacion oficiales del proyecto ocurren en GitHub Actions.
- El disparador esperado es `commit + push` de cambios relevantes del codigo.
- La validacion local es auxiliar y no reemplaza el resultado del workflow remoto.

## Siguiente fase recomendada

- Agregar firma de release segura via secrets para distribucion final.
- Anadir job posterior de smoke test en dispositivo/emulador.
- Recoleccion automatica de `logcat`, `gfxinfo` y `meminfo`.
