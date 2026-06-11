# API Clients

## Purpose
Explain JQuiver API client metadata and safe handling of external client access.

## When to use this file
Use this file when configuring or reviewing external system access to JQuiver APIs.

## Related files
- `07-dynamic-rest-api.md`
- `25-authentication-authorization-flow.md`
- `28-integration-patterns.md`
- `../playbooks/configure-api-client.md`
- `../reference/security-permission-matrix.md`

## Known facts
- Observed HRS metadata includes `jq_api_client_details`.
- Observed fields include `client_id`, `client_name`, `client_key`, `client_secret`, `client_public_key`, `encryption_algo_id`, and `inclusion_url_pattern`.
- Client secrets and keys were present in SQL export data and must be treated as credentials.

## Conceptual behavior
API clients represent external applications or systems that can call selected JQuiver APIs. Access may depend on client key/secret, public key/encryption settings, URL inclusion patterns, route security, and API-specific configuration.

## Relationships
- API client -> client key/secret.
- API client -> encryption algorithm.
- API client -> allowed URL pattern.
- API client -> Dynamic REST route/API behavior.
- API client -> environment-specific secret management.

## Safe AI-agent usage
- Never print or commit real client secrets.
- Rotate credentials if they have been exposed outside approved channels.
- Verify inclusion URL pattern before broadening API access.
- Verify whether the target API is secured and role-restricted.
- Use placeholders in docs: `<CLIENT_KEY>`, `<CLIENT_SECRET>`, `<PUBLIC_KEY>`.

## TODO items to verify
- TODO: verify API client authentication flow from source.
- TODO: verify encryption algorithm lookup and request signing/encryption behavior.
- TODO: verify whether API clients map to roles, scopes, or only URL patterns.
- TODO: verify rotation and revocation process.

## Example
Before enabling an external PM tool integration, inspect the API client row, allowed URL pattern, Dynamic REST routes being called, and whether secrets are environment-specific.
