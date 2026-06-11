---
name: jquiver-additional-datasource
description: Use for JQuiver additional datasource analysis, configuration, migration, and troubleshooting involving jq_additional_datasource, jq_datasource_lookup, datasource_id consumers, and external JDBC databases.
---

# JQuiver Additional Datasource

## 1. Skill name
JQuiver Additional Datasource

## 2. Purpose
Guide an agent through safe analysis or configuration of JQuiver additional datasource metadata and all metadata consumers that reference `datasource_id`.

## 3. Trigger phrases / when to use
- "configure additional datasource"
- "custom schema datasource"
- "external database connection"
- "datasource_id issue"
- "grid/form/API using another database"
- "troubleshoot datasource"

## 4. Required context
- Target instance and environment.
- Database product, if known.
- Intended datasource name.
- Consumer type: form, grid, autocomplete, dashlet, Dynamic REST DAO, or file bin.
- Whether credentials/config values may be inspected.

## 5. Files to read first
- `../../knowledge-base/13-additional-datasource.md`
- `../../knowledge-base/28-integration-patterns.md`
- `../../playbooks/configure-additional-datasource.md`
- `../../developer-runbook/troubleshoot-datasource.md`
- `../../reference/metadata-table-reference.md`
- `../../developer-runbook/safe-data-handling.md`

## 6. Step-by-step workflow
1. Confirm whether the task is read-only analysis or an approved configuration change.
2. Inspect `jq_datasource_lookup` for supported database products.
3. Inspect `jq_additional_datasource` for the target datasource name and ID.
4. Redact URL, username, password, host, token, and secret values.
5. List all consumers referencing the datasource ID.
6. Verify SQL dialect compatibility for each consumer query.
7. Validate connectivity only in an approved environment.
8. For changes, recommend backup before metadata updates.
9. Test one consumer of each type: grid, form, API, autocomplete, dashlet, or file bin.
10. Record unresolved details with `TODO: Verify from actual JQuiver source code / database / instance export.`

## 7. Output format
Return:
- Datasource name and ID.
- Database product.
- Redacted configuration summary.
- Consumer list by metadata table.
- Risks and TODOs.
- Validation steps or rollback notes.

## 8. Safety rules
- Never print credentials or full JDBC URLs with secrets.
- Never generate destructive SQL.
- Always recommend backup before datasource metadata changes.
- Treat production connectivity tests as side-effecting unless explicitly approved.

## 9. Examples
- ARK uses an additional datasource for its custom schema.
- HRS uses additional datasource rows for MariaDB/MySQL/SQL Server-style JDBC connections.

## 10. Things not to do
- Do not copy datasource IDs across instances without verifying target rows.
- Do not assume SQL syntax is portable across database products.
- Do not expose credentials from SQL dumps.
- Do not update consumers before validating the datasource row.
