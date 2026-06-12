# Security, Users, and Roles

## Purpose
Explain JQuiver security metadata at a safe conceptual level.

## When to use this file
Use this file when analyzing login, users, roles, permissions, menu access, entity permissions, public routes, or secured APIs.

## Related files
- `25-authentication-authorization-flow.md`
- `03-module-router.md`
- `../reference/security-permission-matrix.md`
- `../playbooks/add-role-based-menu-access.md`

## Known facts
- Observed user metadata uses `jq_user`.
- Observed role metadata uses `jq_role`.
- Observed user-role association uses `jq_user_role_association`.
- Observed entity-role association uses `jq_entity_role_association`.
- Some Dynamic REST rows include an `is_secured` field.
- Observed authentication metadata includes `jq_authentication_type`.
- Observed security metadata includes `jq_security_type` and `jq_security_properties`; both are verified in the supplied `hrsdev.sql` export.

## Security model concepts
JQuiver security can involve:
- Authentication: who the user is.
- Role membership: what roles a user has.
- Route/menu access: what pages appear or can be opened.
- Entity permissions: what metadata-backed entities a role can access.
- API security: whether Dynamic REST endpoints are secured.
- Anonymous/public behavior: public forms, public job listings, and embedded forms.

## User management
Authentication:
- Verify active authentication mode before creating or modifying users.
- Observed authentication types include database, LDAP, OAuth, and SAML style metadata, but active behavior must be verified from configuration/source.
- Do not assume local database users are authoritative if LDAP/OAuth/SAML is active.

User creation:
- Confirm whether users are created manually, imported, synchronized, or provisioned by external identity provider.
- Verify required profile fields, status flags, password/credential behavior, and email uniqueness from source/database.
- Do not generate or reset credentials unless explicitly asked.

Role creation:
- Define role purpose, owning business area, and expected permissions before creating a role.
- Check for an existing role with the same responsibility.
- Verify role naming convention and migration/export behavior.

Permission management:
- Route/menu access and entity permissions may require multiple metadata rows.
- Verify both page visibility and backing API/grid/file access.
- Every new JQuiver screen/action must include role/entity/module access metadata according to the existing RBAC pattern.
- Minimum expectation: Admin/Super Admin full access, normal/project users functional access, and viewer/read-only roles read access if such roles exist.
- For secured Dynamic REST APIs, confirm `is_secured`, role association, API client behavior, and caller context.
- For file downloads, verify the route/API and the file bin access path.

## Relationships
- User -> role association.
- Role -> entity/route permission.
- Route -> visible menu/page access.
- API -> security flag and route behavior.
- Form/grid/file -> route and entity permissions.

## Safe AI-agent usage
- Do not expose password, OTP, secret key, or user email data.
- Verify public access before recommending anonymous routes.
- Check both route permissions and API permissions.
- Treat file download routes as sensitive.
- Recommend backup before changing role metadata.
- Prefer read-only permission diagnostics before any metadata update.
- Never broaden access to `ALL`, admin-like, or public roles without explicit approval.

## TODO items to verify
- TODO: verify authentication modes from source and configured instances.
- TODO: verify Keycloak support and flow.
- TODO: verify permission evaluation order.
- TODO: verify password and secret storage behavior.
- TODO: verify exact user active/locked/deleted flags and password policy.
- TODO: verify exact role/entity permission schemas by JQuiver version.

## Example
Adding menu access is not just adding a visible label. Verify the route, target metadata, entity-role associations, API calls, and file permissions used by the page.
