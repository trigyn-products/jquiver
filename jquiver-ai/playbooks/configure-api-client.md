# Configure API Client

## Goal
Configure or review an external API client for JQuiver API access.

## When to use
Use this playbook when an external application must call JQuiver Dynamic REST APIs through client credentials or encrypted access.

## Required inputs
- Client name.
- Allowed API route patterns.
- Target environment.
- Authentication/encryption requirements.
- Contact/owner for the external system.
- Credential rotation plan.

## Files/tables/configuration to inspect first
- `jq_api_client_details`.
- `jq_dynamic_rest_details`.
- `jq_dynamic_rest_role_association`.
- `jq_request_type_details`.
- `jq_response_producer_details`.
- `knowledge-base/33-api-clients.md`.
- `reference/security-permission-matrix.md`.

## Step-by-step implementation approach
1. Identify target Dynamic REST APIs and side effects.
2. Verify whether each API should be externally callable.
3. Define the narrowest inclusion URL pattern.
4. Choose or verify encryption/authentication settings.
5. Create or update client metadata only after backup.
6. Store secrets through the approved secret process, not in documentation.
7. Test with a non-production client secret.
8. Test denied routes to confirm the inclusion pattern is not too broad.
9. Document owner, rotation interval, and revocation steps.

## Validation checklist
- Client row exists.
- Secret/key is not exposed in docs or logs.
- Inclusion URL pattern is narrow.
- Target APIs respond as expected.
- Non-target APIs are denied.
- Rotation and revocation steps are known.

## Common mistakes
- Using `/*` when only one API path is needed.
- Reusing production client secrets in development.
- Forgetting `is_secured` and route permission checks.
- Logging client secrets during tests.

## Rollback plan
- Disable or remove the client row after backup.
- Rotate exposed credentials immediately.
- Restore prior inclusion URL pattern.
- Notify external system owner if access changes.

## Related skills and reference docs
- `../knowledge-base/33-api-clients.md`
- `../knowledge-base/07-dynamic-rest-api.md`
- `../knowledge-base/28-integration-patterns.md`
- `../reference/security-permission-matrix.md`
