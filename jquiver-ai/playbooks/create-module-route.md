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

## Step-by-step implementation approach
1. Verify route alias is not already used.
2. Verify target metadata exists.
3. Determine target lookup/type from similar working route.
4. Define menu label and i18n rows.
5. Define parent/sequence/menu visibility.
6. Add role/entity permission metadata after backup.
7. Test route directly.
8. Test route through menu.
9. Test with allowed and disallowed users.

Example read-only route availability check:

```sql
-- Example only.
SELECT module_id, module_url
FROM jq_module_listing
WHERE module_url = '<ROUTE_URL>';
```

## Validation checklist
- Route unique.
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

