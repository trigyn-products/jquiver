# Troubleshoot Datasource

## Purpose
Troubleshoot JQuiver additional datasource issues safely.

## Prerequisites
- Read access to metadata.
- Permission to inspect datasource configuration.
- Credentials must remain redacted.
- Target consumer identified, such as grid, form, API, dashlet, or autocomplete.

## Inputs required
- Datasource name or ID: `<DATASOURCE_ID>`.
- Consumer metadata name or ID: `<CONSUMER_ID>`.
- Target custom schema/table/view: `<TARGET_OBJECT>`.
- Environment name: `<ENVIRONMENT_NAME>`.
- Error message or symptom.

## Steps
1. Identify the failing consumer.
2. Inspect the consumer metadata for `datasource_id`.
3. Inspect `jq_additional_datasource` for the datasource row.
4. Redact connection configuration before sharing.
5. Verify datasource product lookup in `jq_datasource_lookup`.
6. Verify target database/schema exists in the environment.
7. Verify the target table/view/query exists.
8. Run a safe read-only test query if permitted.
9. Check application logs for connection or SQL errors.
10. Check whether datasource IDs differ between environments.
11. Document whether the issue is connection, credential, schema, query, permission, or runtime behavior.

## Validation checklist
- Datasource row exists.
- Datasource is active/not deleted.
- DB product lookup valid.
- Target schema/table/view exists.
- Consumer references correct datasource ID.
- Query works read-only.
- Credentials not exposed.

## Common errors
- Wrong datasource ID after migration.
- Custom schema not restored.
- Target view missing.
- Credentials expired.
- Driver/product mismatch.
- Query written for the wrong database dialect.

## Rollback/safety notes
- Back up metadata before changing datasource rows.
- Do not overwrite credentials from another environment.
- For production, test datasource fixes in lower environment first.
- If reverting, restore previous datasource metadata and restart/retest only as required by runtime behavior.

## Related KB/reference/playbook files
- `../knowledge-base/13-additional-datasource.md`
- `../reference/environment-config-reference.md`
- `../playbooks/configure-additional-datasource.md`
- `common-sql-diagnostics.md`
- `safe-data-handling.md`

