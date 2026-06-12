# Dynamic REST Examples

## Status
Illustrative unless explicitly marked as observed from exported metadata.

## Purpose
Provide generic examples for documenting JQuiver Dynamic REST APIs safely.

## Example 1: Read-Only List API

Use case:
- Return a filtered list of active records for a UI dropdown.

Illustrative metadata:
- URL: `active-record-options`
- HTTP method: `GET`
- Response producer: `application/json`
- Secured: yes
- Side effects: none

Illustrative DAO query:

```sql
-- Example only. Verify parameter binding and SQL dialect first.
SELECT record_id AS value, record_name AS label
FROM active_record_view
WHERE record_name LIKE CONCAT('%', :searchText, '%')
ORDER BY record_name
LIMIT :startIndex, :pageSize;
```

## Example 2: Scheduler Target API

Use case:
- A scheduler calls an API that sends reminder emails.

Documentation shape:

```text
API URL: reminder-mail-job
Caller: jq_job_scheduler
Side effects: sends email
Environment rule: keep inactive in local/dev unless intentionally testing
Security: TODO verify scheduler route behavior and is_secured handling
```

Safety:
- Do not call this API during analysis.
- Test only with a test recipient list.

## Example 3: Activity Log API

Use case:
- API writes or reads activity log data through external service logic.

Observed export pattern:
- HRS had activity-log Dynamic REST methods that referenced CosmosDB-style HTTP logic.
- This appeared in Dynamic REST service logic, not as a JDBC additional datasource row.

Safe documentation:
- "External activity log integration is implemented in Dynamic REST service logic. Endpoint details and credentials are redacted."

## Platform Notes

Observed HRS Dynamic REST editor metadata listed platform choices:
- Java
- FTL
- JavaScript content / Nashorn-style server-side JavaScript
- Python
- PHP

TODO: Verify exact platform IDs and runtime support in the target JQuiver source/version.

API links should use `{api.path}/{api-path}` from `application.yml`/`application.yaml`, defaulting to `/api`. Do not hardcode `/cf/*` for Dynamic REST or Form Builder save calls unless that prefix is configured or verified from an existing working module.
