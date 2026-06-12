# Create Grid

## Goal
Create a JQuiver grid/listing page backed by a table or view.

## When to use
Use this playbook when adding list/search/table pages for business data, reports, or module management.

## Required inputs
- Grid name.
- Backing table/view.
- Column list.
- Filters/sort requirements.
- Datasource ID if custom schema.
- Route/menu requirements.
- Row actions, if any.

## Files/tables/configuration to inspect first
- `jq_grid_details`.
- Backing table/view definition.
- `jq_additional_datasource` if needed.
- Similar working grid.
- Route/template that will host the grid.
- `application.yml` or `application.yaml` for configured `view.path`.

## Step-by-step implementation approach
1. Verify backing table/view exists.
2. Confirm columns and display names.
3. Use GridUtil/grid metadata; do not create Spring Boot CRUD classes unless explicitly asked.
4. Confirm filters and default ordering.
5. Confirm datasource ID if custom schema.
6. Read `application.yml` or `application.yaml`; determine `view.path`, defaulting to `/view`.
7. Identify target router path from module/router metadata.
8. Add grid metadata after backup.
9. Add route/template that renders the grid.
10. Build View/Edit/Open links as `{view.path}/{router-path}` and copy row parameter patterns only from an existing verified module.
11. If a grid action opens a Form Builder page, verify the target form's action/cancel/back URL pattern and do not hardcode `/cf/*`.
12. Add role permissions.
13. Test load, pagination, filters, sorting, and row actions.
14. Review performance with realistic data volume.

Example read-only validation SQL:

```sql
-- Example only. Verify object and column names first.
SELECT <column_list>
FROM <table_or_view>
WHERE 1 = 1
LIMIT 10;
```

## Validation checklist
- Grid metadata exists.
- `view.path` verified or defaulted to `/view`.
- Grid View/Edit/Open links use `{view.path}/{router-path}`.
- Grid actions opening Form Builder pages use verified form route/navigation patterns.
- `{router-path}` matches route metadata.
- Backing table/view valid.
- Datasource valid.
- Columns render.
- Filters work.
- Sort works.
- Pagination works.
- Permissions correct.
- Performance acceptable.

## Common mistakes
- Pointing grid to a missing view.
- Using wrong datasource.
- Column list mismatch.
- No indexes for frequent filters.
- Row action links not secured.
- Row action links using invented `/cf/*` prefixes.
- Creating Spring Boot list APIs/classes instead of JQuiver grid metadata.

## Rollback plan
- Restore previous `jq_grid_details` row if modified.
- Remove new route/menu access if grid must be hidden.
- Revert backing view only after approval and backup.

## Related skills and reference docs
- `../skills/jquiver-grid-utils/SKILL.md`
- `../knowledge-base/06-grid-utils.md`
- `../knowledge-base/29-performance-guidelines.md`
- `../developer-runbook/troubleshoot-grids.md`
