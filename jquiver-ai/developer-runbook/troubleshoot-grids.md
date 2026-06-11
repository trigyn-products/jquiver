# Troubleshoot Grids

## Purpose
Troubleshoot JQuiver grid/listing page issues.

## Prerequisites
- Target grid or route identified.
- Read access to metadata and backing schema.
- Permission to inspect logs.
- Backup plan for metadata changes.

## Inputs required
- Grid name or ID: `<GRID_ID>`.
- Route URL: `<ROUTE_URL>`.
- Backing table/view: `<TABLE_OR_VIEW>`.
- Datasource ID, if known.
- Symptom: no rows, wrong rows, slow load, filter error, sort error, permission issue, or action failure.

## Steps
1. Identify the route/template that uses the grid.
2. Inspect `jq_grid_details`.
3. Verify grid table/view name.
4. Verify the table/view exists in the correct schema.
5. Verify datasource ID if present.
6. Verify column list matches the table/view.
7. Verify custom filter criteria.
8. Run a safe read-only query against the backing object.
9. Check pagination/filter/sort request behavior if available.
10. Check role/menu/entity permissions.
11. Check row action links or APIs if action buttons fail.
12. Check query performance if the grid is slow.

## Validation checklist
- Grid metadata exists.
- Backing object exists.
- Datasource valid.
- Columns valid.
- Filters valid.
- Read-only query returns expected rows.
- Permissions valid.
- Performance acceptable.

## Common errors
- Backing view missing after migration.
- Wrong datasource ID.
- Column list references removed column.
- Custom filter criteria invalid.
- Grid points to default schema but table exists in custom schema.
- User lacks route or entity permission.

## Rollback/safety notes
- Back up `jq_grid_details` before edits.
- Keep previous grid column/filter values.
- Avoid changing backing views in production without backup and test.
- For performance fixes, validate query plan before adding indexes.

## Related KB/reference/playbook files
- `../knowledge-base/06-grid-utils.md`
- `../knowledge-base/29-performance-guidelines.md`
- `../playbooks/create-grid.md`
- `../playbooks/modify-existing-module.md`
- `troubleshoot-datasource.md`

