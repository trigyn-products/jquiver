# Additional Datasource

## Purpose
Explain how JQuiver connects metadata to multiple databases or schemas.

## When to use this file
Use this file when configuring or troubleshooting cross-schema grids, forms, APIs, autocompletes, dashlets, or integrations.

## Related files
- `02-core-database-model.md`
- `28-integration-patterns.md`
- `../playbooks/configure-additional-datasource.md`
- `../developer-runbook/troubleshoot-datasource.md`
- `../reference/environment-config-reference.md`

## Known facts
- Observed datasource metadata uses `jq_additional_datasource`.
- Observed datasource product lookup uses `jq_datasource_lookup`.
- ARK has datasource rows for `ARK_DOT_NET` and `ARK CUSTOM`.
- `ARK CUSTOM` points to the custom MariaDB schema `ark_pharma_knowledge_base_custom`.
- SBI FAC has a custom datasource for the custom careers schema.
- Metadata consumers can include grids, forms, Dynamic REST DAO rows, autocompletes, and dashlets.

## Datasource lifecycle
1. Datasource metadata defines DB product and connection configuration.
2. Metadata consumers reference the datasource ID.
3. Runtime opens/uses the connection when executing the consumer query.
4. Query results feed the grid, form, API, autocomplete, or dashlet.

## Relationships
- `jq_additional_datasource` -> `jq_datasource_lookup`.
- Grid/form/API/autocomplete/dashlet -> datasource ID.
- Datasource -> custom schema objects.
- Environment/security policy -> credential handling.

## Safe AI-agent usage
- Never print datasource passwords.
- Redact connection URLs if they expose hostnames or credentials.
- Verify target schema and table/view existence.
- Check whether datasource IDs differ between environments.
- Recommend backup before changing datasource metadata.

## TODO items to verify
- TODO: verify password encryption or secret management behavior from source code.
- TODO: verify connection pooling and validation behavior.
- TODO: verify supported database products by version.
- TODO: verify environment-specific datasource migration practice.

## Example
In ARK, `ManufacturersGrid`, `Manufacturers-form`, and several ARK autocompletes use the `ARK CUSTOM` datasource to query the custom schema.

