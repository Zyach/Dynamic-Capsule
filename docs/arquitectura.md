# Arquitectura

Fecha de referencia: 2026-03-13

## Vision general

La app es una aplicacion Android basada en Jetpack Compose cuyo comportamiento principal depende de dos servicios privilegiados:

- `IslandOverlayService`: servicio de accesibilidad que dibuja la interfaz flotante.
- `NotificationService`: listener de notificaciones que alimenta eventos para plugins.

## Componentes principales

- `MainActivity`: shell de configuracion y onboarding.
- `IslandSettings`: estado persistido de posicion, tamano y comportamiento de la isla.
- `PluginManager`: orquesta plugins activos y prioridad de visualizacion.
- `BasePlugin`: contrato comun para plugins UI/comportamiento.
- Plugins actuales: `notification`, `media`, `battery`.

## Riesgos arquitectonicos actuales

- Estado global con singletons y referencias estaticas.
- Logica de dominio mezclada con servicios Android y composables.
- Registro de plugins duplicado entre `PluginManager` y `ExportedPlugins`.
- Gestion manual del ciclo de vida Compose en el overlay.

## Direccion objetivo

- Mantener una sola fuente de verdad para plugins.
- Reducir estado global mutable.
- Aislar logica testeable fuera de servicios Android.
- Centralizar eventos internos con canales seguros y documentados.
