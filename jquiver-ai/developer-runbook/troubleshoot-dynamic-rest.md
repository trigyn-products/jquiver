# Troubleshoot Dynamic REST

## Purpose
Troubleshoot JQuiver Dynamic REST API failures safely.

## Prerequisites
- API URL or method name identified.
- Read access to Dynamic REST metadata.
- Permission to inspect logs.
- Understanding of whether API has side effects.

## Inputs required
- API URL: `<API_URL>`.
- Method name: `<METHOD_NAME>`.
- HTTP method/request type.
- Request payload sample with sensitive data redacted.
- Error response or log excerpt.
- User/security context.

## Steps
1. Identify the Dynamic REST metadata row.
2. Verify URL, method name, request type, and response producer.
3. Verify security flag and required access.
4. Inspect service logic.
5. Inspect DAO query rows.
6. Verify datasource IDs used by DAO rows.
7. Identify side effects such as save, delete, email, file download, or scheduler behavior.
8. Run only read-only tests unless explicitly authorized.
9. Check application logs for stack traces or query errors.
10. Check caller page/template/form for payload shape.
11. Verify response format expected by the UI.
12. If a metadata edit is required, recommend backup and follow the Dynamic REST playbook.

## Validation checklist
- API metadata exists.
- Request type correct.
- Response producer correct.
- Security behavior understood.
- DAO queries valid.
- Datasource valid.
- Side effects identified.
- UI expected payload/response shape confirmed.

## Common errors
- Calling POST API with GET.
- Wrong response producer type.
- Missing request parameter.
- DAO query references wrong datasource.
- Service logic has unhandled null or missing field.
- API is unsecured when it should be secured.
- API sends email or writes data during testing.

## Rollback/safety notes
- Back up `jq_dynamic_rest_details` and DAO rows before edits.
- Do not call side-effecting APIs in production for testing.
- Keep prior service logic and DAO query values for rollback.
- If an API caused data changes, plan data rollback separately.

## Related KB/reference/playbook files
- `../knowledge-base/07-dynamic-rest-api.md`
- `../playbooks/create-dynamic-rest-api.md`
- `../knowledge-base/25-authentication-authorization-flow.md`
- `troubleshoot-datasource.md`
- `common-sql-diagnostics.md`

