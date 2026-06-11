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

## Step-by-step implementation approach
1. Verify backing table/view exists.
2. Confirm columns and display names.
3. Confirm filters and default ordering.
4. Confirm datasource ID if custom schema.
5. Add grid metadata after backup.
6. Add route/template that renders the grid.
7. Add role permissions.
8. Test load, pagination, filters, sorting, and row actions.
9. Review performance with realistic data volume.

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

## Rollback plan
- Restore previous `jq_grid_details` row if modified.
- Remove new route/menu access if grid must be hidden.
- Revert backing view only after approval and backup.

## Related skills and reference docs
- `../skills/jquiver-grid-utils/SKILL.md`
- `../knowledge-base/06-grid-utils.md`
- `../knowledge-base/29-performance-guidelines.md`
- `../developer-runbook/troubleshoot-grids.md`

