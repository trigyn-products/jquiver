---
name: jquiver-api-client
description: Use for JQuiver API client analysis and configuration involving jq_api_client_details, client keys/secrets, encryption settings, inclusion URL patterns, and secured Dynamic REST access.
---

# JQuiver API Client

## 1. Skill name
JQuiver API Client

## 2. Purpose
Guide an agent through safe API client review, creation, testing, and revocation planning for external systems that call JQuiver APIs.

## 3. Trigger phrases / when to use
- "API client"
- "client key"
- "client secret"
- "external system access"
- "API integration credentials"
- "inclusion URL pattern"
- "secured REST client"

## 4. Required context
- External system name and owner.
- Target Dynamic REST API routes.
- Allowed URL pattern.
- Security/encryption requirement.
- Environment and credential handling policy.

## 5. Files to read first
- `../../knowledge-base/33-api-clients.md`
- `../../knowledge-base/07-dynamic-rest-api.md`
- `../../knowledge-base/25-authentication-authorization-flow.md`
- `../../playbooks/configure-api-client.md`
- `../../reference/security-permission-matrix.md`

## 6. Step-by-step workflow
1. Confirm the external system and target APIs.
2. Inspect `jq_api_client_details` for existing clients.
3. Inspect target `jq_dynamic_rest_details` rows and side effects.
4. Verify whether each API is secured and should be externally callable.
5. Define the narrowest inclusion URL pattern.
6. Verify encryption algorithm requirements and public key handling.
7. Redact all keys and secrets in notes.
8. Test allowed routes in non-production.
9. Test denied routes to confirm the pattern is not too broad.
10. Document rotation and revocation steps.

## 7. Output format
Return:
- Client purpose.
- Target API list.
- Redacted credential status.
- URL pattern review.
- Security risks.
- Test and rollback plan.

## 8. Safety rules
- Never print or commit client secrets.
- Rotate credentials if exposed.
- Use placeholders such as `<CLIENT_KEY>` and `<CLIENT_SECRET>`.
- Do not broaden access without explicit approval.

## 9. Examples
- A PMTool integration may require an API client and a limited Dynamic REST route pattern.
- A public integration must still be reviewed for API side effects.

## 10. Things not to do
- Do not use `/*` unless explicitly approved.
- Do not reuse production secrets in test environments.
- Do not document real private keys.
- Do not assume API client access replaces role/API security review.
