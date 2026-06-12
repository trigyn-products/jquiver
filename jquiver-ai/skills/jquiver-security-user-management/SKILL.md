---
name: jquiver-security-user-management
description: Use for JQuiver authentication, user creation, role creation, permission management, route/menu access, secured APIs, public access checks, user-role associations, and entity-role associations.
---

# JQuiver Security User Management

## 1. Skill name
JQuiver Security User Management

## 2. Purpose
Guide an agent through safe review or configuration of JQuiver authentication, users, roles, and permissions.

## 3. Trigger phrases / when to use
- "user management"
- "authentication"
- "create user"
- "create role"
- "permission management"
- "menu access"
- "secured API"
- "public access"

## 4. Required context
- Target user/role/access request.
- Authentication mode.
- Target route/API/entity.
- Intended audience.
- Environment and approval status.

## 5. Files to read first
- `../../knowledge-base/15-security-users-roles.md`
- `../../knowledge-base/25-authentication-authorization-flow.md`
- `../../playbooks/add-role-based-menu-access.md`
- `../../reference/security-permission-matrix.md`
- `../../developer-runbook/safe-data-handling.md`

## 6. Step-by-step workflow
1. Verify active authentication mode.
2. Inspect `jq_user`, `jq_role`, and association metadata as needed.
3. Inspect route/menu metadata.
4. Inspect backing API/grid/form/file permissions.
5. For every new screen/action, add role/entity/module access metadata according to the existing RBAC pattern.
6. Minimum expectation: Admin/Super Admin full access, normal/project users functional access, viewer/read-only roles read access if such roles exist.
7. Verify public/anonymous access requirements.
8. Test with representative roles.
9. Confirm unauthorized access is denied.
10. Preserve previous role/permission rows before changes.

## 7. Output format
Return:
- Access map.
- User/role/association summary.
- Route/API/entity permission findings.
- Missing permissions.
- Risks/TODOs.
- Test and rollback plan.

## 8. Safety rules
- Do not expose passwords, OTPs, secrets, or full user lists.
- Do not broaden access without explicit approval.
- Verify both visible menu and backend access.
- Recommend backup before permission changes.

## 9. Examples
- Adding menu access also requires checking target APIs, grids, forms, and file downloads.
- Public Form.io routes need explicit anonymous access review.

## 10. Things not to do
- Do not assume database users are authoritative under LDAP/OAuth/SAML.
- Do not grant admin-like roles as a shortcut.
- Do not forget secured REST API associations.
- Do not expose private file downloads through route changes.
