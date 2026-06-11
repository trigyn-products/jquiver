# Common SQL Diagnostics

## Purpose
Provide safe read-only SQL diagnostic workflow for JQuiver metadata and custom schemas.

## Prerequisites
- Read-only database access preferred.
- Target schema names identified.
- Sensitive-data handling rules understood.
- Permission to inspect metadata.

## Inputs required
- Platform schema: `<JQUIVER_SCHEMA>`.
- Custom schema list: `<CUSTOM_SCHEMA_LIST>`.
- Target module or symptom.
- Known route/form/grid/API names, if available.
- Database engine: `<DB_ENGINE>`.

## Steps
1. Confirm the database engine and target schemas.
2. List tables and views in the platform schema.
3. List custom domain tables and views.
4. Count rows in key metadata tables.
5. Inspect route metadata for the affected module.
6. Inspect target metadata, such as form, grid, API, dashlet, scheduler, or file bin.
7. Inspect datasource IDs and target custom schemas.
8. Inspect role/entity permission rows if access-related.
9. Inspect backing table/view definitions for grid/report issues.
10. Capture only safe summaries, not raw sensitive rows.
11. Mark unknown query type IDs or semantics as TODO.

## Validation checklist
- Diagnostics are read-only.
- Target schemas confirmed.
- Sensitive columns not printed.
- Route and target metadata mapped.
- Datasource dependencies mapped.
- Permissions considered.
- Findings include TODOs for unverified semantics.

## Common errors
- Running write queries during diagnosis.
- Dumping full applicant/user/file tables.
- Ignoring additional datasource references.
- Querying the wrong schema.
- Assuming all `jq_` tables have same columns across versions.

## Rollback/safety notes
- Read-only diagnostics do not need rollback.
- If a diagnostic accidentally changes state, stop and report immediately.
- For production, avoid heavy queries without approval.

## Related KB/reference/playbook files
- `database-schema-analysis.md`
- `jquiver-metadata-navigation.md`
- `../reference/metadata-table-reference.md`
- `../knowledge-base/23-jquiver-metadata-tables-reference.md`
- `safe-data-handling.md`

