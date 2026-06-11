# JQuiver Metadata Tables Reference

## Purpose
Provide a safe v1 reference for important observed JQuiver metadata tables.

## When to use this file
Use this file when an agent needs to identify which metadata tables may control a page, API, form, grid, scheduler, dashboard, or integration.

## Related files
- `02-core-database-model.md`
- `24-jquiver-page-lifecycle.md`
- `../reference/metadata-table-reference.md`

## Known facts
This file documents JQuiver core/platform and metadata tables only. Custom module/business tables belong in instance summary or instance-specific reference files.

JQuiver core/platform tables are identified as:
- `jq_*`
- `flyway_schema_history`
- `mail_schedule`
- `persistent_logins`
- `qrtz_*`

Observed JQuiver metadata/core tables include:

- Routing: `jq_module_listing`, `jq_module_listing_i18n`.
- Forms: `jq_dynamic_form`, `jq_dynamic_form_save_queries`.
- Form.io: `jq_form_io`, `jq_formio_route`.
- Grids: `jq_grid_details`.
- Dynamic REST: `jq_dynamic_rest_details`, `jq_dynamic_rest_dao_details`, `jq_dynamic_rest_role_association`.
- Request/response lookups: `jq_request_type_details`, `jq_response_producer_details`.
- Autocomplete/typeahead: `jq_autocomplete_details`.
- Datasources: `jq_additional_datasource`, `jq_datasource_lookup`.
- File upload: `jq_file_upload_config`, `jq_file_upload`.
- Dashboard/dashlet: `jq_dashboard`, `jq_dashlet`.
- Templates: `jq_template_master`.
- Script libraries: `jq_script_lib_details`, `jq_script_lib_connect`.
- API clients: `jq_api_client_details`.
- Notifications: `jq_generic_user_notification`.
- Business modules: `jq_business_module`, `jq_business_module_entity_details`.
- Tags: `jq_tag_data`, `jq_tag_entity_details`.
- Multilingual/resource text: `jq_resource_bundle`.
- Configuration: `jq_property_master`.
- Authentication/security: `jq_user`, `jq_role`, `jq_user_role_association`, `jq_entity_role_association`, `jq_authentication_type`, `jq_security_type`, `jq_security_properties`; `jq_security_type` and `jq_security_properties` are verified in `hrsdev.sql`.
- Mail/template history: `jq_template_master`, `jq_failed_mail_history`, `jq_mail_history_data`.
- Mail schedule: `mail_schedule`.
- Flyway migration history: `flyway_schema_history`.
- Authentication/session support: `persistent_logins`.
- Quartz scheduler tables: `qrtz_*`.
- Scheduler: `jq_job_scheduler`.

## How to use this reference
Start with the user-visible symptom:
- Page issue: route, target metadata, permissions.
- Form issue: form row, save queries, datasource, route.
- Grid issue: grid row, table/view, datasource, filters.
- API issue: Dynamic REST row, DAO rows, security, service logic.
- File issue: file bin and file metadata.
- Dashboard issue: dashboard, dashlets, queries.
- Datasource issue: datasource row and consumers.
- Script library issue: library row, template/body dependency, connection rows, all consumers.
- API client issue: client row, secret handling, inclusion URL pattern, API route permissions.
- Notification issue: notification row, audience criteria, validity window, target platform.
- Business module/tag issue: grouping rows, referenced metadata, permission JSON, checksums.

## Safe AI-agent usage
- These are observed table names, not a full official catalog.
- Do not add ARK, SBI FAC, HRS, or other business tables to this metadata/core reference.
- Verify exact columns in the target database.
- Verify source migrations before claiming version-wide behavior.
- Avoid writing to these tables without backup.

## TODO items to verify
- TODO: verify full table list from source migrations.
- TODO: verify required columns and nullable fields by version.
- TODO: verify deprecated tables.
- TODO: verify stored procedures and import/export support tables.

## Example
A broken public page may involve `jq_module_listing`, `jq_template_master`, `jq_grid_details`, `jq_dynamic_rest_details`, `jq_resource_bundle`, and `jq_entity_role_association`.
