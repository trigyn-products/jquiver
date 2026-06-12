# Create Module Route

## Goal
Create or configure a JQuiver route/menu entry that points to the correct target.

## When to use
Use this playbook when exposing a form, grid page, dashboard, template, API, external URL, or root/menu grouping.

## Required inputs
- Route URL/alias.
- Target type.
- Target ID.
- Menu label.
- Parent menu, if any.
- Sequence/order.
- Roles allowed.
- Layout inclusion requirement.

## Files/tables/configuration to inspect first
- `jq_module_listing`.
- `jq_module_listing_i18n`.
- `jq_entity_role_association`.
- Target metadata table.
- Existing route with similar target type.
- `application.yml` or `application.yaml` for configured `view.path`.

## Step-by-step implementation approach
1. Verify route alias is not already used.
2. Read `application.yml` or `application.yaml`; determine `view.path`, defaulting to `/view`.
3. Verify target metadata exists.
4. Determine target lookup/type from similar working route.
5. Define menu label and i18n rows.
6. Define parent/sequence/menu visibility.
7. Build navigation links as `{view.path}/{router-path}`.
8. If the route opens a Form Builder screen, copy cancel/back/action navigation from an existing verified form and do not hardcode `/cf/*`.
9. Add role/entity permission metadata after backup.
10. Test route directly.
11. Test route through menu.
12. Test with allowed and disallowed users.

Example read-only route availability check:

```sql
-- Example only.
SELECT module_id, module_url
FROM jq_module_listing
WHERE module_url = '<ROUTE_URL>';
```

## Validation checklist
- Route unique.
- `view.path` verified from config or defaulted to `/view`.
- Menu/router link uses `{view.path}/{router-path}`.
- Form Builder route links reuse a verified navigation pattern when applicable.
- Target metadata exists.
- Menu label renders.
- Route loads.
- Layout behavior correct.
- Role access correct.
- No broken links or missing assets.

## Common mistakes
- Reusing an existing route URL.
- Wrong target lookup/type.
- Missing i18n label.
- Missing role/entity permission.
- Inventing `/cf/*` without configured or verified `view.path`.
- Incorrect parent/sequence causing menu issues.

## Rollback plan
- Remove or disable new route metadata using approved reversible change.
- Restore previous menu sequence if changed.
- Remove associated permission rows.
- Keep target metadata unless separately created and approved for rollback.

## Related skills and reference docs
- `../knowledge-base/03-module-router.md`
- `../knowledge-base/24-jquiver-page-lifecycle.md`
- `../reference/metadata-table-reference.md`
- `../developer-runbook/jquiver-metadata-navigation.md`
