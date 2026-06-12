# Analyze Existing Instance

## Goal
Analyze a JQuiver instance folder, SQL export, custom schema, upload folder, or reachable local URL safely.

## When to use
Use this playbook when learning an unknown JQuiver instance, preparing documentation, comparing environments, or building an AI knowledge base.

## Required inputs
- Instance folder path.
- SQL export files.
- Custom schema files.
- Upload folder path, if analysis is allowed.
- Local URL, if available.
- `application.yml` or `application.yaml`, if route/API link validation is in scope.
- User constraints.

## Files/tables/configuration to inspect first
- SQL export headers for database names.
- `jq_module_listing`.
- `jq_dynamic_form`.
- `jq_grid_details`.
- `jq_dynamic_rest_details`.
- `jq_dynamic_rest_dao_details`.
- `jq_additional_datasource`.
- `application.yml` or `application.yaml` for `view.path` and `api.path`, if allowed by user constraints.
- `jq_file_upload_config`, `jq_file_upload`.
- `jq_dashboard`, `jq_dashlet`.
- JQuiver core/platform tables: `jq_*`, `flyway_schema_history`, `mail_schedule`, `persistent_logins`, `qrtz_*`.
- Custom module/business tables and views.

## Step-by-step implementation approach
1. Record user constraints.
2. Inventory files and folders.
3. Identify SQL exports and schema names.
4. Count tables, views, and populated tables.
5. Separate JQuiver core/platform tables from custom module/business tables using the approved core-table rule.
6. Map routes, forms, grids, APIs, datasources, dashboards, schedulers, file bins, and autocompletes.
7. If allowed, read `application.yml` or `application.yaml` and record configured `view.path` and `api.path`; defaults are `/view` and `/api`.
8. Map custom tables to forms, grids, Dynamic REST APIs, dashboards, and datasource consumers.
9. Summarize upload folders without exposing private files.
10. Check local URL using safe GET requests if provided.
11. Produce structure summary and TODOs.

Output format:
- Core JQuiver tables found.
- Missing expected core tables, if any.
- Custom module tables found.
- Business modules inferred.
- Tables linked to forms/grids/APIs.
- Configured `view.path` and `api.path`, or defaults used.
- Unmapped custom tables.
- Assumptions/TODOs.

Example read-only route count:

```sql
-- Example only.
SELECT COUNT(*) AS route_count
FROM jq_module_listing;
```

## Validation checklist
- Constraints followed.
- SQL files read.
- Schemas identified.
- Metadata tables mapped.
- Custom schema mapped.
- Datasource usage mapped.
- Uploads summarized safely.
- Runtime URL checked read-only if available.
- Sensitive data redacted.

## Common mistakes
- Creating a new dump when user asked not to.
- Reading forbidden config files.
- Exploding JARs without permission.
- Printing credentials or PII.
- Treating service-layer access as full UI validation.
- Treating custom module/business tables as JQuiver core tables.

## Rollback plan
- This playbook is read-only.
- If generated intermediate files were created accidentally, remove them with user awareness.
- If sensitive data was written to an artifact, sanitize it immediately.

## Related skills and reference docs
- `../skills/jquiver-instance-analyzer/SKILL.md`
- `../developer-runbook/local-analysis-runbook.md`
- `../developer-runbook/database-schema-analysis.md`
- `../knowledge-base/02-core-database-model.md`
- `../developer-runbook/safe-data-handling.md`
