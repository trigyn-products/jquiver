# JQuiver Core Tables

## Purpose
Define the approved table-name rule for JQuiver core/platform tables.

## When to use this file
Use this file when classifying tables from a SQL export.

## Related files
- `verified-schema-index.md`
- `table-to-feature-map.md`
- `metadata-table-reference.md`

## Known facts
Only the following table names/patterns are treated as JQuiver core/platform tables:
- `jq_*`
- `flyway_schema_history`
- `mail_schedule`
- `persistent_logins`
- `qrtz_*`

All other tables are custom module/business tables unless explicitly documented otherwise.

## TODO items to verify
- TODO: Verify whether future JQuiver versions add new non-`jq_*` core table names.

