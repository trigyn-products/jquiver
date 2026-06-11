# Security Permission Matrix

## Purpose
Provide a placeholder matrix for roles, routes, entities, APIs, and files.

## When to use this file
Use this file before adding or changing menu access, public routes, file bins, or APIs.

## Related files
- `../knowledge-base/15-security-users-roles.md`
- `../knowledge-base/25-authentication-authorization-flow.md`
- `../playbooks/add-role-based-menu-access.md`

## Known facts
- Observed role metadata uses `jq_role`.
- Observed entity permission metadata uses `jq_entity_role_association`.
- Some dynamic REST rows include `is_secured`.

## TODO items to verify
- TODO: Verify exact permission model from source code.
- TODO: Verify role type IDs and entity role behavior.

## Example
Document whether a route is anonymous, authenticated, role-limited, or entity-permission-limited.

