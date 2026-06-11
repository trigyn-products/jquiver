# Database Schema Analysis

## Purpose
Guide detailed read-only analysis of JQuiver database schemas and SQL exports.

## Prerequisites
- SQL exports or database read access available.
- Target schemas identified.
- Sensitive-data handling rules understood.
- No destructive SQL required.

## Inputs required
- Platform SQL export: `<PLATFORM_SQL_EXPORT>`.
- Custom SQL export list: `<CUSTOM_SQL_EXPORT_LIST>`.
- Database engine: `<DB_ENGINE>`.
- Target instance: `<INSTANCE_NAME>`.
- Analysis scope: `<ANALYSIS_SCOPE>`.

## Steps
1. Identify JQuiver core tables using the approved core-table rule: `jq_*`, `flyway_schema_history`, `mail_schedule`, `persistent_logins`, and `qrtz_*`.
2. Identify custom module tables: all other non-core tables and views.
3. Map custom tables to modules/forms/grids/dynamic REST APIs using metadata relationships.
4. Mark uncertain mappings as TODO.
5. Identify database names from SQL headers or connection metadata.
6. List create-table blocks and views at schema level only.
7. Count tables, views, and populated objects without copying row data.
8. Identify populated metadata tables.
9. Extract foreign keys and unique keys for custom module tables.
10. Map `jq_additional_datasource` records and redact credentials.
11. Map metadata consumers using datasource IDs.
12. Identify file upload bins and file metadata counts.
13. Identify route, form, grid, API, scheduler, dashboard, and autocomplete metadata.
14. Summarize findings by module and schema.
15. Add TODOs where source/runtime verification is still needed.

## Validation checklist
- Main schema identified.
- Custom schemas identified.
- Table and view counts captured.
- Row counts captured.
- Metadata tables mapped.
- Custom module/business tables mapped.
- Datasource usage mapped.
- Credentials redacted.
- PII redacted.

## Common errors
- Misparsing SQL inserts that contain semicolons in templates/scripts.
- Ignoring views because they appear as create-table placeholders in dumps.
- Treating row counts as business truth without runtime verification.
- Exposing connection strings.
- Exposing applicant/user data.
- Treating every observed table as a JQuiver core/platform table.

## Rollback/safety notes
- Schema analysis is read-only.
- Do not load dumps into a live database without approval.
- If importing into a local database for analysis, use an isolated local schema.

## Related KB/reference/playbook files
- `common-sql-diagnostics.md`
- `jquiver-metadata-navigation.md`
- `../knowledge-base/02-core-database-model.md`
- `../knowledge-base/23-jquiver-metadata-tables-reference.md`
- `../reference/metadata-table-reference.md`
