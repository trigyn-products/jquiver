# Configure Home Page

## Goal
Define or modify the JQuiver home page route safely.

## When to use
Use this playbook when a tenant, role, or instance should land on a specific dashboard, template, form, or page after login.

## Required inputs
- Target route/module ID.
- Target route URL.
- Intended audience.
- Whether the change is global, role-specific, or context-specific.
- Existing home page metadata.

## Files/tables/configuration to inspect first
- `jq_module_listing`.
- `jq_module_listing_i18n`.
- Role/entity permission metadata.
- Target page metadata, such as dashboard/template/form/grid.
- `knowledge-base/03-module-router.md`.
- `knowledge-base/24-jquiver-page-lifecycle.md`.

## Step-by-step implementation approach
1. Identify current home page route candidates.
2. Verify target route renders successfully.
3. Verify target route is accessible to intended users.
4. Confirm target route does not require missing query parameters.
5. Back up current route metadata.
6. Update the home-page flag only after approval.
7. Test login/landing behavior with representative roles.
8. Test direct navigation to the old and new pages.

## Validation checklist
- Exactly the intended route is marked as home page.
- Intended users can access it.
- Unauthorized users do not gain access.
- Page loads without required missing parameters.
- Menu/highlight behavior remains acceptable.

## Common mistakes
- Setting a form/action route as home page.
- Choosing a route hidden from the target role.
- Leaving multiple conflicting home pages.
- Forgetting mobile or low-permission users.

## Rollback plan
- Restore previous `is_home_page` values.
- Re-test login with the affected roles.
- Keep old route available until users confirm the new landing page.

## Related skills and reference docs
- `../knowledge-base/03-module-router.md`
- `../knowledge-base/24-jquiver-page-lifecycle.md`
- `../reference/security-permission-matrix.md`
- `../developer-runbook/database-backup-restore.md`
