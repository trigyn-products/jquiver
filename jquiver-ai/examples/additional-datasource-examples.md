# Additional Datasource Examples

## Status
Illustrative unless explicitly marked as observed from exported metadata. Do not treat these examples as production-ready SQL or configuration.

## Purpose
Show safe patterns for describing JQuiver additional datasource usage without exposing credentials.

## Example 1: Custom Reporting Schema

Scenario:
- A grid needs to read from a reporting schema that is separate from the main JQuiver metadata schema.

Illustrative metadata relationship:
- `jq_additional_datasource.additional_datasource_id`: `<REPORTING_DS_ID>`
- `jq_additional_datasource.datasource_name`: `Reporting DB`
- `jq_datasource_lookup.database_product_name`: `mariadb`
- `jq_grid_details.datasource_id`: `<REPORTING_DS_ID>`

Safe documentation:
- "The grid reads from a reporting datasource. The datasource configuration exists and must be verified in the target environment."

Do not document:
- JDBC URL with username/password.
- Hostnames, production database names, or secrets.

## Example 2: Autocomplete Against External HR Data

Scenario:
- A typeahead field searches employee names from an external HR database.

Illustrative query shape:

```sql
-- Example only. Verify table, column, parameter, and SQL dialect first.
SELECT employee_id AS value, display_name AS label
FROM employee_directory
WHERE display_name LIKE CONCAT('%', :searchText, '%')
ORDER BY display_name
LIMIT :startIndex, :pageSize;
```

Safety notes:
- Return only fields required by the UI.
- Avoid exposing phone, salary, address, personal email, or identity document values.
- Confirm the datasource ID before migration.

## Example 3: Dynamic REST DAO Query

Scenario:
- A Dynamic REST API needs a read-only lookup from an external project database.

Illustrative relationship:
- `jq_dynamic_rest_details.jws_dynamic_rest_url`: `project-summary`
- `jq_dynamic_rest_dao_details.datasource_id`: `<PROJECT_DS_ID>`
- `jq_dynamic_rest_dao_details.jws_dao_query_type`: TODO: verify lookup value.

Validation checklist:
- Datasource exists in the target environment.
- Query runs read-only.
- API response redacts sensitive project/customer data if needed.
- API security reviewed.

## Observed Export Pattern

Observed from prior instance exports:
- Additional datasource metadata is stored in `jq_additional_datasource`.
- Product lookup metadata is stored in `jq_datasource_lookup`.
- Consumers may reference `datasource_id` from grids, forms, autocompletes, Dynamic REST DAO rows, dashlets, and file bins.

TODO: Verify exact columns and behavior from the target JQuiver version.
