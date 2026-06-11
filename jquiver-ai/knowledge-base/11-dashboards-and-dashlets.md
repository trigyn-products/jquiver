# Dashboards and Dashlets

## Purpose
Explain JQuiver dashboards and dashlets as metadata-driven reporting and summary components.

## When to use this file
Use this file when creating, modifying, analyzing, or troubleshooting dashboards, metrics, widgets, or chart-like summaries.

## Related files
- `13-additional-datasource.md`
- `29-performance-guidelines.md`
- `../playbooks/create-dashboard-and-dashlet.md`
- `../examples/dashboard-dashlet-examples.md`

## Known facts
- Observed dashboard metadata uses `jq_dashboard`.
- Observed dashlet metadata uses `jq_dashlet`.
- SBI FAC includes dashlets for open jobs, applicants, scheduled interviews, selected candidates, and panel metrics.
- Dashlets may use SQL queries and datasource IDs.

## Dashboard lifecycle
1. Route resolves to a dashboard target.
2. Dashboard metadata identifies layout and behavior.
3. Associated dashlets load.
4. Dashlet queries or bodies execute/render.
5. Frontend displays summary counts, charts, cards, tables, or custom content.

## Relationships
- Dashboard -> dashlets.
- Dashlet -> query/body.
- Dashlet -> datasource ID.
- Dashboard route -> permissions.
- Dashlet data -> business tables or views.

## Safe AI-agent usage
- Treat dashboard queries as potentially expensive.
- Verify datasource and schema before editing queries.
- Check whether metrics expose sensitive data.
- Avoid running aggregate queries on large tables without filters/index awareness.
- Do not assume chart type semantics without source verification.

## TODO items to verify
- TODO: verify dashboard-dashlet association tables by version.
- TODO: verify supported dashlet/chart rendering types.
- TODO: verify permissions for dashboard visibility.
- TODO: verify export behavior.

## Example
A "total applicants" dashlet should be checked for correct datasource, query filters, role visibility, and performance on production-size applicant data.

