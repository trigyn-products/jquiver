# Configure Additional Datasource

## Goal
Configure a JQuiver additional datasource and connect metadata consumers to it.

## When to use
Use this playbook when forms, grids, APIs, dashlets, or autocompletes need to query a custom schema or external database.

## Required inputs
- Datasource name.
- Database product.
- Target database/schema.
- Environment-specific connection placeholders.
- Metadata consumers.
- Credential handling method.

## Files/tables/configuration to inspect first
- `jq_additional_datasource`.
- `jq_datasource_lookup`.
- Consumers: `jq_grid_details`, `jq_dynamic_form`, `jq_dynamic_rest_dao_details`, `jq_autocomplete_details`, `jq_dashlet`.
- Environment configuration and secret storage policy.

## Step-by-step implementation approach
1. Verify target database product is supported.
2. Verify target schema and objects exist.
3. Verify secret handling policy.
4. Create or update datasource metadata after backup.
5. Redact credentials in documentation.
6. Connect only required metadata consumers.
7. Test read-only query through each consumer.
8. Validate behavior in the target environment.

Example read-only consumer search:

```sql
-- Example only.
SELECT grid_id, grid_name
FROM jq_grid_details
WHERE datasource_id = '<DATASOURCE_ID>';
```

## Validation checklist
- Datasource row active.
- Product lookup valid.
- Credentials stored according to policy.
- Target schema reachable.
- Consumers reference correct datasource ID.
- Queries work.
- No secrets exposed.

## Common mistakes
- Copying credentials between environments.
- Using a datasource ID from another environment.
- Forgetting custom schema migration.
- Wrong database driver/product.
- Exposing connection strings.

## Rollback plan
- Restore previous datasource metadata.
- Repoint consumers to previous datasource only if safe and backed up.
- Disable affected route/grid/API if datasource cannot be restored quickly.

## Related skills and reference docs
- `../skills/jquiver-additional-datasource/SKILL.md`
- `../knowledge-base/13-additional-datasource.md`
- `../developer-runbook/troubleshoot-datasource.md`
- `../reference/environment-config-reference.md`

