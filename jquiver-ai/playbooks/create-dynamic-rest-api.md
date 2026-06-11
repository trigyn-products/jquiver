# Create Dynamic REST API

## Goal
Create a JQuiver Dynamic REST API safely.

## When to use
Use this playbook when UI behavior, integrations, scheduler jobs, or custom workflows need a metadata-defined endpoint.

## Required inputs
- API URL.
- Method name.
- HTTP method.
- Request payload fields.
- Response format.
- Execution platform: Java, FTL, JavaScript/Nashorn-style content, Python, PHP, or TODO if unverified.
- Service logic requirements.
- DAO queries and datasource IDs.
- Security requirement.
- Side effect classification.

## Files/tables/configuration to inspect first
- `jq_dynamic_rest_details`.
- `jq_dynamic_rest_dao_details`.
- `jq_request_type_details`.
- `jq_response_producer_details`.
- `jq_script_lib_details` and `jq_script_lib_connect` if reusable scripts are attached.
- `jq_additional_datasource` if DAO uses custom schema.
- Similar working API.

## Step-by-step implementation approach
1. Confirm the API is needed and cannot be handled by existing metadata.
2. Find a similar API in the target instance.
3. Define request/response contract.
4. Choose execution platform based on the closest working API and verify source support.
5. Define security behavior, including `is_secured`, role access, and API client access if needed.
6. Identify side effects.
7. Write DAO queries with parameters where supported.
8. Use additional datasource only when required.
9. Attach script libraries only after listing all dependencies.
10. Add API metadata after backup.
11. Test with safe non-production payloads.
12. Test error behavior.
13. Document caller expectations.

Example read-only API inventory SQL:

```sql
-- Example only.
SELECT jws_dynamic_rest_id, jws_dynamic_rest_url, jws_method_name, is_secured
FROM jq_dynamic_rest_details
WHERE jws_dynamic_rest_url = '<API_URL>';
```

## Validation checklist
- API URL unique.
- HTTP method correct.
- Response type correct.
- Security reviewed.
- Execution platform verified.
- Script library dependencies checked.
- DAO queries work.
- Datasource valid.
- Side effects expected.
- Error handling tested.
- Caller page/API client tested.

## Common mistakes
- Unsecured API by accident.
- Wrong request type.
- Returning HTML where JSON is expected.
- DAO query using wrong datasource.
- Testing side-effecting API in production.
- Assuming JavaScript content is browser JavaScript instead of server-side execution.
- Creating Python/PHP APIs without verifying runtime support.

## Rollback plan
- Restore previous API metadata and DAO rows.
- Disable route/API if immediate rollback is needed.
- Revert any dependent scheduler/template/form caller changes.
- Roll back data side effects separately if they occurred.

## Related skills and reference docs
- `../skills/jquiver-dynamic-rest/SKILL.md`
- `../knowledge-base/07-dynamic-rest-api.md`
- `../developer-runbook/troubleshoot-dynamic-rest.md`
- `../reference/metadata-table-reference.md`
