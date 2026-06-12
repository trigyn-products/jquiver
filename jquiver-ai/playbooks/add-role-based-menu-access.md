# Add Role-Based Menu Access

## Goal
Grant or adjust role-based access to a JQuiver menu/route safely.

## When to use
Use this playbook when a role needs access to a page, menu item, entity, grid, form, dashboard, or related module.

## Required inputs
- Role name/ID: `<ROLE_ID>`.
- Route/module URL or ID: `<MODULE_ID>`.
- Entity target, if applicable.
- Access type required.
- Environment and approval.

## Files/tables/configuration to inspect first
- `jq_role`.
- `jq_module_listing`.
- `jq_entity_role_association`.
- `jq_user_role_association`.
- `jq_module_listing_i18n`.
- Security matrix reference.
- Existing access metadata for a similar working screen/action.

## Step-by-step implementation approach
1. Verify the role exists and is active.
2. Verify the route/module exists.
3. Verify current access for the role.
4. Inspect similar access rows for the same module type.
5. For new screens/actions, map Admin/Super Admin full access, normal/project-user functional access, and viewer/read-only access if such roles exist.
6. Confirm whether API/file access also needs changes.
7. Prepare reversible metadata change after backup.
8. Test with a user who has the role.
9. Test with a user who should not have the role.

Example read-only access check:

```sql
-- Example only. Verify exact columns first.
SELECT role_id, role_name, is_active
FROM jq_role
WHERE role_name = '<ROLE_NAME>';
```

## Validation checklist
- Role exists.
- Route exists.
- New screen/action has role/entity/module access metadata.
- Admin/Super Admin has full access where appropriate.
- Normal/project users have functional access where appropriate.
- Viewer/read-only roles have read-only access if such roles exist.
- Menu appears for intended role.
- Menu hidden or blocked for unintended role.
- Target page loads.
- API/file actions on the page are also authorized correctly.

## Common mistakes
- Granting menu access but not API access.
- Granting broad access through the wrong role.
- Using role IDs from another environment.
- Forgetting entity-level permission rows.

## Rollback plan
- Restore previous role/entity association rows from backup.
- Remove newly added permission rows using approved reversible script.
- Re-test intended and unintended roles.

## Related skills and reference docs
- `../knowledge-base/15-security-users-roles.md`
- `../knowledge-base/25-authentication-authorization-flow.md`
- `../reference/security-permission-matrix.md`
- `../developer-runbook/common-sql-diagnostics.md`
