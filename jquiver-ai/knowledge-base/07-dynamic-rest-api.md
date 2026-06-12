# Dynamic REST API

## Purpose
Explain JQuiver Dynamic REST APIs and how agents should analyze them safely.

## When to use this file
Use this file when creating, modifying, documenting, or troubleshooting metadata-defined APIs.

## Related files
- `13-additional-datasource.md`
- `14-scheduler.md`
- `25-authentication-authorization-flow.md`
- `../playbooks/create-dynamic-rest-api.md`
- `../developer-runbook/troubleshoot-dynamic-rest.md`

## Known facts
- Observed API metadata uses `jq_dynamic_rest_details`.
- Observed DAO/query metadata uses `jq_dynamic_rest_dao_details`.
- Observed request types include HTTP verbs such as GET and POST.
- Observed HRS request type lookup values include POST, GET, PATCH, PUT, DELETE, OPTIONS, HEAD, TRACE, and ALL.
- Observed response producer types include JSON, HTML, text, octet-stream, XML, and email/XML.
- Observed HRS dynamic REST editor metadata lists platform choices for Java, FTL, JavaScript content, Python, and PHP.
- Observed dynamic REST metadata includes `is_secured`, `jws_allow_files`, `hide_dao_query`, `jws_header_json`, and `is_custom_updated`.
- SBI FAC includes APIs such as `jdo`, `jddes`, and interview-related APIs.
- ARK includes APIs such as `arkdt` and `atsd`.
- HRS includes activity-log APIs such as `logActivity`, `getActivityLog`, `activityLog`, and `getTopUsageActivityLog`; CosmosDB activity log behavior appears in service logic, not as a normal JDBC additional datasource row.

## Execution platforms
Java:
- Use when the platform invokes Java-backed behavior or source-implemented handlers.
- Verify exact class/method binding from source before documenting or changing behavior.

FTL:
- Use for FreeMarker-style templating/service logic.
- Verify variable bindings such as request details, params, and result objects before editing.

Python:
- Observed as a platform option in HRS metadata.
- TODO: verify runtime engine, allowed libraries, sandboxing, and deployment requirements from source.

Nashorn JavaScript / JavaScript content:
- Observed HRS service logic uses JavaScript-style code with `Java.type(...)`, which is consistent with Nashorn-style Java interop.
- Treat this as powerful server-side code, not browser JavaScript.
- TODO: verify actual JavaScript engine and supported Java runtime version from source.

PHP:
- Observed as a platform option in HRS metadata.
- TODO: verify whether PHP execution is enabled in the target JQuiver version.

## Secured REST API
- `is_secured` appears in observed metadata and should be reviewed for every public or integration-facing API.
- Security is not only the flag. Verify route exposure, API client requirements, role associations, headers, and caller context.
- A scheduler may call APIs through scheduler-specific routes, so secured runtime behavior must be tested separately from browser calls.

## API lifecycle
1. Client calls a Dynamic REST route.
2. JQuiver resolves API metadata.
3. Request type and response producer are validated.
4. Service logic and/or DAO queries execute.
5. Queries may use the default datasource or an additional datasource.
6. Response is produced as JSON, HTML, text, file stream, email/XML, or another configured type.

## Relationships
- API -> request type.
- API -> response producer.
- API -> service logic.
- API -> DAO query rows.
- DAO row -> datasource ID.
- Scheduler -> API target.
- UI page -> API calls.

## Safe AI-agent usage
- Use JQuiver Dynamic REST metadata first; do not create Spring Boot REST endpoints, Controller, Service, Repository/DAO, DTO, request/response, or Entity classes unless explicitly asked or verified metadata patterns are insufficient.
- Do not call APIs until side effects are known.
- Identify whether the API sends email, saves data, deletes data, schedules work, or downloads files.
- Verify `is_secured` and route access.
- Redact payloads containing PII or secrets.
- Prefer source/export analysis before runtime calls.
- Treat JavaScript/Nashorn, Python, Java, and PHP service logic as server-side executable code.
- For FTL and XML responses, validate escaping and output type.
- For API client use, verify client credentials and never copy secrets into docs.

## TODO items to verify
- TODO: verify Dynamic REST execution platforms and service logic types from source code.
- TODO: verify request payload binding rules.
- TODO: verify authentication and authorization handling.
- TODO: verify file upload/download behavior for APIs.
- TODO: verify exact platform IDs for Java, FTL, JavaScript/Nashorn, Python, and PHP.
- TODO: verify secured API behavior for scheduler calls and API clients.

## Example
An API named like a cleanup or reminder job may send emails or delete records. Treat it as side-effecting until verified otherwise.
