---
name: jquiver-dashboard-dashlet
description: Use for JQuiver dashboard and dashlet analysis, configuration, or troubleshooting involving jq_dashboard, jq_dashlet, dashboard-dashlet associations, role associations, datasource-backed dashlet queries, and reporting performance.
---

# JQuiver Dashboard Dashlet

## 1. Skill name
JQuiver Dashboard Dashlet

## 2. Purpose
Guide an agent through safe creation, review, and troubleshooting of JQuiver dashboards and dashlets.

## 3. Trigger phrases / when to use
- "dashboard"
- "dashlet"
- "metric card"
- "chart widget"
- "dashboard route"
- "reporting widget"
- "dashboard not loading"

## 4. Required context
- Dashboard name/ID.
- Dashlet name/ID.
- Required metric or visualization.
- Query datasource.
- Intended roles/audience.
- Performance expectations.

## 5. Files to read first
- `../../knowledge-base/11-dashboards-and-dashlets.md`
- `../../knowledge-base/13-additional-datasource.md`
- `../../knowledge-base/29-performance-guidelines.md`
- `../../playbooks/create-dashboard-and-dashlet.md`
- `../../examples/dashboard-dashlet-examples.md`
- `../../reference/metadata-table-reference.md`

## 6. Step-by-step workflow
1. Inspect `jq_dashboard` and `jq_dashlet`.
2. Inspect dashboard-dashlet association rows.
3. Verify route/menu metadata for dashboard access.
4. Review each dashlet query and datasource ID.
5. Check role associations and page permissions.
6. Evaluate query performance and indexing.
7. Test dashboard rendering with intended roles.
8. Validate empty-state and large-data behavior.

## 7. Output format
Return:
- Dashboard/dashlet map.
- Queries and datasource IDs.
- Association and role summary.
- Performance risks.
- TODOs and validation checklist.

## 8. Safety rules
- Do not expose sensitive aggregate breakdowns to unauthorized roles.
- Use bounded/aggregated queries for dashlets.
- Verify datasource connectivity before changing dashlet metadata.
- Recommend backup before metadata writes.

## 9. Examples
- HRS has a "Module Data" dashboard with dashlets for active modules, users, and top interviewers.
- SBI FAC includes dashboard/dashlet patterns for HR metrics.

## 10. Things not to do
- Do not use unbounded row-level queries for dashboards.
- Do not assume a dashlet query uses the default datasource.
- Do not forget dashboard route access.
- Do not migrate dashlets without association rows.
