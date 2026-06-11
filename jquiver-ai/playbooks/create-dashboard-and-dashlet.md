# Create Dashboard and Dashlet

## Goal
Create or modify a JQuiver dashboard and dashlet for summary metrics or reporting.

## When to use
Use this playbook when users need metric cards, charts, summary widgets, or dashboard pages.

## Required inputs
- Dashboard name.
- Dashlet name.
- Metric/query definition.
- Target roles.
- Datasource ID if custom schema.
- Layout requirements.
- Refresh/export expectations.

## Files/tables/configuration to inspect first
- `jq_dashboard`.
- `jq_dashlet`.
- Dashboard-dashlet association metadata. TODO: verify from database/source.
- `jq_additional_datasource` if needed.
- Similar working dashboard/dashlet.

## Step-by-step implementation approach
1. Decide whether the requirement is dashboard, grid, or report.
2. Verify source data and aggregation logic.
3. Write bounded/read-only metric query.
4. Verify query performance.
5. Configure dashlet metadata after backup.
6. Associate dashlet with dashboard using verified association pattern.
7. Create or verify dashboard route.
8. Add permissions.
9. Test layout, data correctness, and performance.

Example read-only metric query:

```sql
-- Example only. Verify source table/view first.
SELECT COUNT(*) AS total_count
FROM <safe_reporting_view>
WHERE <status_column> = '<SAMPLE_STATUS>';
```

## Validation checklist
- Metric matches source data.
- Query is bounded and performant.
- Dashlet renders.
- Dashboard renders.
- Permissions correct.
- Datasource correct.
- No sensitive data exposed.

## Common mistakes
- Expensive unfiltered aggregate query.
- Wrong datasource.
- Missing dashboard association.
- Metric visible to wrong role.
- Dashboard route missing.

## Rollback plan
- Restore previous dashboard/dashlet metadata.
- Remove new dashlet association.
- Disable dashboard route if needed.
- Revert reporting query/view separately if changed.

## Related skills and reference docs
- `../skills/jquiver-dashboard-dashlet/SKILL.md`
- `../knowledge-base/11-dashboards-and-dashlets.md`
- `../knowledge-base/29-performance-guidelines.md`
- `../examples/dashboard-dashlet-examples.md`

