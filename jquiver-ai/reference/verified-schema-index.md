# Verified Schema Index

## Purpose
Classify verified SQL-export tables into JQuiver core/platform tables and custom module/business tables.

## When to use this file
Use this file before mapping an instance database or deciding whether a table belongs to JQuiver core.

## Related files
- `jquiver-core-tables.md`
- `table-to-feature-map.md`
- `metadata-table-reference.md`
- `../knowledge-base/02-core-database-model.md`

## Known facts
JQuiver core/platform tables are identified by this rule:
- `jq_*`
- `flyway_schema_history`
- `mail_schedule`
- `persistent_logins`
- `qrtz_*`

All other tables are custom module/business tables unless explicitly documented otherwise.

## Schema Classification
JQuiver Core Tables:
- All `jq_*` tables.

Scheduler / Quartz Tables:
- All `qrtz_*` tables.
- Scheduler metadata remains in `jq_job_scheduler` and related `jq_*` tables.

Authentication / Session Tables:
- `persistent_logins`.
- Authentication and user metadata in `jq_*` tables.

Mail Schedule Tables:
- `mail_schedule`.
- Mail/template metadata and history in `jq_*` tables.

Flyway Migration Table:
- `flyway_schema_history`.

Custom Module Tables:
- Any table not matching `jq_*`, `flyway_schema_history`, `mail_schedule`, `persistent_logins`, or `qrtz_*`.
- Examples include ARK, SBI FAC, HRS, and other instance-specific business tables.

## TODO items to verify
- TODO: Verify full custom table inventories per instance export.
- TODO: Verify exact columns and runtime behavior from source/application.

