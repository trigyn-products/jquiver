---
name: jquiver-dynamic-rest
description: Use for JQuiver Dynamic REST API analysis, creation, or troubleshooting involving jq_dynamic_rest_details, jq_dynamic_rest_dao_details, request/response types, Java/FTL/Python/Nashorn-style JavaScript platforms, secured APIs, file-enabled APIs, schedulers, and API clients.
---

# JQuiver Dynamic REST

## 1. Skill name
JQuiver Dynamic REST

## 2. Purpose
Guide an agent through safe analysis, creation, or troubleshooting of metadata-defined JQuiver REST APIs.

## 3. Trigger phrases / when to use
- "Dynamic REST"
- "REST API"
- "secured REST API"
- "jws_dynamic_rest"
- "DAO query"
- "Nashorn JavaScript"
- "FTL API"
- "scheduler API target"

## 4. Required context
- API URL/method name.
- HTTP method.
- Response producer type.
- Execution platform: Java, FTL, JavaScript/Nashorn-style, Python, PHP, or unknown.
- Request fields.
- DAO queries and datasource IDs.
- Side-effect classification.
- Security requirement.

## 5. Files to read first
- `../../knowledge-base/07-dynamic-rest-api.md`
- `../../knowledge-base/26-deployment-and-environment.md`
- `../../knowledge-base/13-additional-datasource.md`
- `../../knowledge-base/25-authentication-authorization-flow.md`
- `../../playbooks/create-dynamic-rest-api.md`
- `../../developer-runbook/troubleshoot-dynamic-rest.md`
- `../../reference/metadata-table-reference.md`

## 6. Step-by-step workflow
1. Inspect `jq_dynamic_rest_details` for URL, method, request type, producer type, platform, security, and file flags.
2. Inspect `jq_dynamic_rest_dao_details` for DAO queries and datasource IDs.
3. Inspect role/API client/scheduler consumers if applicable.
4. Read `application.yml` or `application.yaml`; build REST links as `{api.path}/{api-path}`, defaulting to `/api`.
5. Identify side effects before calling the API.
6. Verify platform support from source or a matching working API.
7. Validate request binding and output format.
8. Test in non-production with safe payloads.
9. Review logs and failure behavior.
10. Document rollback rows for metadata changes.

## 7. Output format
Return:
- API map.
- Platform and request/response details.
- DAO/datasource summary.
- Security and caller map.
- Side effects.
- TODOs and tests.

## 8. Safety rules
- Use JQuiver Dynamic REST metadata first; do not create Spring Boot REST endpoints, Controller, Service, Repository/DAO, DTO, request/response, or Entity classes unless explicitly asked or verified metadata patterns are insufficient.
- Do not invoke side-effecting APIs during analysis unless explicitly asked.
- Treat server-side JavaScript/Python/Java/PHP/FTL as executable backend logic.
- Redact PII, tokens, and secrets.
- Recommend backup before metadata changes.

## 9. Examples
- HRS activity log APIs call CosmosDB-style HTTP endpoints from service logic.
- Scheduler jobs may invoke Dynamic REST APIs for reminders or cleanup.

## 10. Things not to do
- Do not assume unsecured APIs are safe.
- Do not test save/delete/mail APIs in production.
- Do not invent non-default API prefixes; verify `api.path` or use `/api`.
- Do not copy platform IDs across versions without verification.
- Do not hide DAO query changes from review.
