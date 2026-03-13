# Permisos y Privacidad

Fecha de referencia: 2026-03-13

## Permisos sensibles usados

- `SYSTEM_ALERT_WINDOW`
- `BIND_ACCESSIBILITY_SERVICE`
- `BIND_NOTIFICATION_LISTENER_SERVICE`
- `QUERY_ALL_PACKAGES`
- `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`

## Riesgos principales

- Overlay y accesibilidad requieren justificacion UX y tecnica muy clara.
- `QUERY_ALL_PACKAGES` puede generar rechazo en Play Store si no se justifica correctamente.
- El trafico interno entre componentes debe mantenerse encapsulado y no exportado salvo necesidad real.

## Medidas tecnicas actuales

- Broadcasts internos ahora se emiten con package scoping.
- Receivers internos dinamicos se registran como `RECEIVER_NOT_EXPORTED`.

## Medidas pendientes

- Revisar si `QUERY_ALL_PACKAGES` puede reducirse o reemplazarse.
- Documentar de cara a usuario final por que se pide cada permiso.
- Anadir politica de privacidad y retention policy tecnica.
