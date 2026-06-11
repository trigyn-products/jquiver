# Authentication and Authorization Flow

## Purpose
Explain the authentication and authorization concepts agents must consider in JQuiver.

## When to use this file
Use this file when troubleshooting access, menus, secured APIs, public pages, file downloads, role permissions, or embedded forms.

## Related files
- `15-security-users-roles.md`
- `03-module-router.md`
- `07-dynamic-rest-api.md`
- `../reference/security-permission-matrix.md`
- `../playbooks/add-role-based-menu-access.md`

## Known facts
- Observed metadata includes users, roles, user-role associations, and entity-role associations.
- Some public Dynamic REST rows in analyzed dumps had `is_secured=0`.
- Public routes exist in SBI FAC for careers/job application behavior.

## Conceptual flow
1. User or anonymous visitor requests a route/API/file.
2. Runtime identifies whether authentication is required.
3. If authenticated, user identity and roles are loaded.
4. Route/API/entity permissions are evaluated.
5. Page or API response is allowed or rejected.
6. UI may hide or show menu items/actions based on permissions.

## Important checks
- Is the route public or authenticated?
- Does the route appear in menu?
- Does the target metadata require role/entity permission?
- Are API calls from the page also secured?
- Are file download URLs protected?
- Does embedded Form.io access bypass normal layout/security assumptions?

## Safe AI-agent usage
- Do not assume route visibility equals authorization.
- Verify API security separately from page security.
- Treat anonymous forms as high-risk.
- Do not expose user/security fields from dumps.
- Recommend security review for public APIs and file routes.

## TODO items to verify
- TODO: verify Keycloak/session support from source code.
- TODO: verify exact authentication modes and configuration keys.
- TODO: verify permission evaluation order.
- TODO: verify anonymous user behavior.

## Example
A public application form may be allowed anonymously, but its resume download API should not be assumed public without explicit verification.

