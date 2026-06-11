# Create Reporting Page

## Goal
Create a reporting page using JQuiver grids, views, templates, dashboards, or dashlets.

## When to use
Use this playbook when users need read-only reporting, metrics, exports, dashboards, or filtered listing pages.

## Required inputs
- Report name.
- Audience/roles.
- Source tables/views.
- Required filters.
- Required columns or metrics.
- Export requirements.
- Performance expectations.

## Files/tables/configuration to inspect first
- Existing reporting pages.
- Business tables and views.
- `jq_grid_details`.
- `jq_dashboard`, `jq_dashlet` if dashboard-style.
- `jq_module_listing`.
- `jq_additional_datasource` if custom schema is used.

## Step-by-step implementation approach
1. Decide whether the report is best as a grid, dashboard, dashlet, template, or API.
2. Design a read-only table/view/query.
3. Verify filters and indexes.
4. Configure grid or dashlet metadata.
5. Create route/menu access.
6. Add resource labels.
7. Add role permissions.
8. Test with realistic row counts.
9. Validate export behavior if required.

Example read-only report query pattern:

```sql
-- Example only. Use only after verifying table/view names.
SELECT <columns>
FROM <reporting_view>
WHERE <safe_filter_column> = '<SAMPLE_VALUE>';
```

## Validation checklist
- Report data matches source records.
- Filters work.
- Pagination works.
- Query performance acceptable.
- Role access correct.
- No sensitive data exposed to unauthorized users.

## Common mistakes
- Using transactional tables directly without filters.
- Returning too much data.
- Ignoring custom datasource latency.
- Giving report access to the wrong role.

## Rollback plan
- Remove/disable route access if report must be hidden.
- Restore previous grid/dashlet metadata.
- Revert reporting view changes only if approved and backed up.
- Keep data changes separate from report metadata rollback.

## Related skills and reference docs
- `../knowledge-base/06-grid-utils.md`
- `../knowledge-base/11-dashboards-and-dashlets.md`
- `../knowledge-base/29-performance-guidelines.md`
- `../skills/jquiver-grid-utils/SKILL.md`
