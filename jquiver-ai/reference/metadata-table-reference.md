# Metadata Table Reference

## Purpose
List important JQuiver metadata tables and their observed purpose.

## When to use this file
Use this file before reading or changing metadata rows.

## Related files
- `../knowledge-base/23-jquiver-metadata-tables-reference.md`
- `../developer-runbook/jquiver-metadata-navigation.md`
- `verified-schema-index.md`
- `jquiver-core-tables.md`
- `table-to-feature-map.md`

## Known facts
- Exact table names are considered verified only when present in one of the supplied SQL exports.
- JQuiver core/platform tables are `jq_*`, `flyway_schema_history`, `mail_schedule`, `persistent_logins`, and `qrtz_*`; all other tables are custom module/business tables unless explicitly documented otherwise.
- Observed route metadata: `jq_module_listing`, `jq_module_listing_i18n`.
- Observed form metadata: `jq_dynamic_form`, `jq_dynamic_form_save_queries`.
- Observed grid metadata: `jq_grid_details`.
- Observed dynamic REST metadata: `jq_dynamic_rest_details`, `jq_dynamic_rest_dao_details`.
- Observed autocomplete metadata: `jq_autocomplete_details`.
- Observed datasource metadata: `jq_additional_datasource`, `jq_datasource_lookup`.
- Observed file metadata: `jq_file_upload_config`, `jq_file_upload`.
- Observed dashboard metadata: `jq_dashboard`, `jq_dashlet`.
- Observed security metadata: `jq_user`, `jq_role`, `jq_user_role_association`, `jq_entity_role_association`.
- Observed Form.io metadata: `jq_form_io`, `jq_formio_route`.
- Observed template metadata: `jq_template_master`.
- Observed script library metadata: `jq_script_lib_details`, `jq_script_lib_connect`.
- Observed API client metadata: `jq_api_client_details`.
- Observed notification metadata: `jq_generic_user_notification`.
- Observed business module metadata: `jq_business_module`, `jq_business_module_entity_details`.
- Observed tag metadata: `jq_tag_data`, `jq_tag_entity_details`.
- Observed request/response lookup metadata: `jq_request_type_details`, `jq_response_producer_details`.
- Observed authentication/security metadata: `jq_authentication_type`, `jq_security_type`, `jq_security_properties`.
- `jq_security_type` and `jq_security_properties` are verified in `hrsdev.sql`.

## Observed metadata clusters
Routing and menu:
- `jq_module_listing`
- `jq_module_listing_i18n`
- `jq_module_role_association`

Forms and Form.io:
- `jq_dynamic_form`
- `jq_dynamic_form_save_queries`
- `jq_form_io`
- `jq_formio_route`

APIs and integrations:
- `jq_dynamic_rest_details`
- `jq_dynamic_rest_dao_details`
- `jq_dynamic_rest_role_association`
- `jq_api_client_details`
- `jq_request_type_details`
- `jq_response_producer_details`
- `jq_script_lib_details`
- `jq_script_lib_connect`

Files, notifications, and mail:
- `jq_file_upload_config`
- `jq_file_upload`
- `jq_generic_user_notification`
- `jq_template_master`
- `jq_failed_mail_history`
- `jq_mail_history_data`
- `mail_schedule`

Organization and migration:
- `jq_business_module`
- `jq_business_module_entity_details`
- `jq_tag_data`
- `jq_tag_entity_details`
- `flyway_schema_history`

Scheduler/session support:
- `qrtz_*`
- `persistent_logins`

## TODO items to verify
- TODO: Verify complete metadata table catalog from source migrations.
- TODO: Verify exact columns by JQuiver version.
- TODO: Verify lookup values and runtime behavior for each metadata cluster.

## Example
Before changing a form, inspect `jq_dynamic_form` and related `jq_dynamic_form_save_queries`.
