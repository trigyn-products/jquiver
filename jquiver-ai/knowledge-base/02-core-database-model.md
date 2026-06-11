# Core Database Model

## Purpose
Explain how JQuiver uses database metadata and custom schemas to define application behavior.

## When to use this file
Use this file before analyzing an export, creating metadata, comparing instances, or writing SQL diagnostics.

## Related files
- `23-jquiver-metadata-tables-reference.md`
- `13-additional-datasource.md`
- `24-jquiver-page-lifecycle.md`
- `../reference/metadata-table-reference.md`

## Known facts
- Observed metadata tables include `jq_module_listing`, `jq_module_listing_i18n`, `jq_dynamic_form`, `jq_dynamic_form_save_queries`, `jq_form_io`, `jq_formio_route`, `jq_grid_details`, `jq_dynamic_rest_details`, `jq_dynamic_rest_dao_details`, `jq_autocomplete_details`, `jq_additional_datasource`, `jq_datasource_lookup`, `jq_file_upload_config`, `jq_file_upload`, `jq_dashboard`, `jq_dashlet`, `jq_template_master`, `jq_resource_bundle`, `jq_property_master`, `jq_user`, and `jq_role`.
- HRS analysis also confirmed metadata families for script libraries, API clients, notifications, business modules, tags, authentication types, and request/response lookups.
- ARK and SBI FAC exports include both JQuiver core/platform tables and custom module/business tables.
- Some metadata rows include checksum, custom-update, created-by, and updated-by fields.
- JQuiver core/platform tables are identified as `jq_*`, `flyway_schema_history`, `mail_schedule`, `persistent_logins`, and `qrtz_*`.
- All other tables in instance SQL exports are custom module/business tables unless explicitly documented otherwise.

## Core concepts
Metadata tables:
- Store definitions for pages, routes, APIs, forms, grids, dashboards, and integrations.
- Are operationally sensitive because changing them can change runtime behavior.
- JQuiver platform metadata is stored primarily in `jq_*` tables.
- Scheduler metadata uses `jq_job_scheduler` and `jq_job_scheduler_log`; Quartz runtime scheduling tables use `qrtz_*`.

Business tables:
- Store domain data for a specific application or client.
- May be queried directly by JQuiver metadata.
- Business applications built on JQuiver create their own custom tables, which do not normally start with `jq_`.

Core support tables:
- Flyway migration history uses `flyway_schema_history`.
- Login/session support may use `persistent_logins`.
- Mail scheduling uses `mail_schedule`.

Views:
- Often simplify listing pages and hide complex joins.
- Examples include ARK policy listing views and SBI job/applicant/interview listing views.

Additional datasources:
- Allow metadata to query a database other than the default application schema.
- Require careful credential and connectivity handling.

## Analysis workflow
1. Identify schemas and SQL files.
2. Count tables, views, and populated tables.
3. Separate JQuiver core/platform tables from custom module/business tables using the approved core-table rule.
4. Map routes to target metadata.
5. Map forms, grids, APIs, dashlets, and autocompletes to datasource IDs.
6. Identify sensitive tables and redact private values.

## Safe AI-agent usage
- Use read-only SQL first.
- Do not infer business rules only from table names.
- Recommend backup before writes.
- Avoid printing credentials or PII from dumps.

## TODO items to verify
- TODO: verify all mandatory metadata tables from source migrations.
- TODO: verify foreign-key behavior and cascade rules by JQuiver version.
- TODO: verify metadata import/export conventions.
- TODO: verify exact meaning of query type IDs.

## Example
Before modifying a route, inspect `jq_module_listing`, its localized label rows, entity-role associations, target metadata, and any query/datasource dependencies.
