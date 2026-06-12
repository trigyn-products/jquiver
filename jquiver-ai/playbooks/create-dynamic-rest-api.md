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
- `application.yml` or `application.yaml` for configured `api.path`.
- Similar working API.

## Step-by-step implementation approach
1. Confirm the API is needed and cannot be handled by existing metadata.
2. Find a similar API in the target instance.
3. Use Dynamic REST metadata; do not create Spring Boot REST endpoints, Controller, Service, Repository/DAO, DTO, request/response, or Entity classes unless explicitly asked.
4. Read `application.yml` or `application.yaml`; build REST links as `{api.path}/{api-path}`, defaulting to `/api`.
5. Define request/response contract.
6. Choose execution platform based on the closest working API and verify source support.
7. Define security behavior, including `is_secured`, role access, and API client access if needed.
8. Identify side effects.
9. Write DAO queries with parameters where supported.
10. Use additional datasource only when required.
11. Attach script libraries only after listing all dependencies.
12. Add API metadata after backup.
13. Test with safe non-production payloads.
14. Test error behavior.
15. Document caller expectations.

Example read-only API inventory SQL:

```sql
-- Example only.
SELECT jws_dynamic_rest_id, jws_dynamic_rest_url, jws_method_name, is_secured
FROM jq_dynamic_rest_details
WHERE jws_dynamic_rest_url = '<API_URL>';
```

## Validation checklist
- API URL unique.
- `api.path` verified or defaulted to `/api`.
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
- Creating Spring Boot REST endpoints/classes instead of Dynamic REST metadata.
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
