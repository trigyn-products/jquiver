# Integration Patterns

## Purpose
Explain common JQuiver integration patterns at a safe conceptual level.

## When to use this file
Use this file for external APIs, additional databases, schedulers, webhooks, embedded forms, file downloads, and cross-application workflows.

## Related files
- `07-dynamic-rest-api.md`
- `13-additional-datasource.md`
- `14-scheduler.md`
- `20-external-form-embedding.md`
- `09-file-upload.md`

## Known facts
- JQuiver can connect metadata to additional datasources.
- JQuiver can expose Dynamic REST APIs.
- JQuiver can embed pluggable forms through iframe routes.
- Schedulers can invoke Dynamic REST APIs.
- ARK includes a datasource pointing to a custom schema and another external SQL Server-style datasource record.
- HRS includes API client metadata in `jq_api_client_details`.
- HRS includes activity-log Dynamic REST logic that calls CosmosDB-style HTTP endpoints from service logic.

## Common patterns
Database integration:
- Use additional datasource metadata.
- Reference datasource ID from grid/form/API/autocomplete/dashlet metadata.

API integration:
- Use Dynamic REST metadata.
- Validate request/response types and security.

Scheduled integration:
- Use scheduler metadata to invoke Dynamic REST API.
- Treat target APIs as side-effecting until verified.

Embedded form integration:
- Use iframe route for Form.io/pluggable forms.
- Verify security and frame behavior.

File integration:
- Use file bins and file metadata.
- Verify file download security.

API client integration:
- Use API client metadata when an external system needs authenticated or encrypted access.
- Treat client keys, secrets, and public keys as credentials.
- Verify inclusion URL pattern and encryption algorithm before enabling a client.

Custom service-logic integration:
- Dynamic REST service logic can call external HTTP services, including activity logging endpoints.
- Verify endpoint URL, headers, authentication, timeout, retry, and failure handling from source/runtime configuration.
- Never expose endpoint secrets or copied request bodies in docs.

## Safe AI-agent usage
- Do not call external APIs or side-effecting endpoints without explicit instruction.
- Redact integration credentials.
- Verify environment-specific URLs.
- Verify retry and timeout behavior before documenting reliability claims.
- Distinguish JDBC additional datasource integrations from HTTP/API integrations.

## TODO items to verify
- TODO: verify webhook support.
- TODO: verify external API client metadata.
- TODO: verify retry, timeout, and circuit-breaker behavior.
- TODO: verify external datasource credential management.
- TODO: verify API client authentication/encryption flow from source.

## Example
A scheduled reminder integration can be modeled as scheduler -> Dynamic REST -> email/API call, but each step must be verified before production use.
