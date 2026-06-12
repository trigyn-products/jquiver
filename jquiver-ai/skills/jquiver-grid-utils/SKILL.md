---
name: jquiver-grid-utils
description: Use for JQuiver Grid Utils analysis, creation, and troubleshooting involving jq_grid_details, pqGrid-style listings, table/view/query backing data, datasource_id, filters, pagination, actions, and performance.
---

# JQuiver Grid Utils

## 1. Skill name
JQuiver Grid Utils

## 2. Purpose
Guide an agent through safe creation, modification, or troubleshooting of JQuiver grid/listing pages.

## 3. Trigger phrases / when to use
- "Grid Utils"
- "grid"
- "listing page"
- "pqGrid"
- "jq_grid_details"
- "grid not loading"
- "custom filter"

## 4. Required context
- Grid ID/name.
- Backing table, view, or query.
- Columns and labels.
- Datasource ID, if any.
- Filters, pagination, sorting, and row actions.
- Intended roles.

## 5. Files to read first
- `../../knowledge-base/06-grid-utils.md`
- `../../knowledge-base/03-module-router.md`
- `../../knowledge-base/24-jquiver-page-lifecycle.md`
- `../../knowledge-base/13-additional-datasource.md`
- `../../knowledge-base/29-performance-guidelines.md`
- `../../reference/environment-config-reference.md`
- `../../playbooks/create-grid.md`
- `../../developer-runbook/troubleshoot-grids.md`
- `../../examples/grid-examples.md`

## 6. Step-by-step workflow
1. Inspect `jq_grid_details`.
2. Verify backing table/view/query exists.
3. Verify datasource ID and SQL dialect.
4. Review selected columns, filters, actions, and custom criteria.
5. Check route/menu and permissions for the grid page.
6. Read `application.yml` or `application.yaml`; build grid View/Edit/Open links as `{view.path}/{router-path}`, defaulting to `/view`.
7. Copy row identifier parameter patterns only from an existing verified JQuiver module.
8. Evaluate query performance and pagination behavior.
9. Test with intended roles and realistic row counts.
10. Validate empty state, sorting, filtering, and action links.

## 7. Output format
Return:
- Grid map.
- Backing data source.
- Columns/filter/action summary.
- Performance risks.
- Permission notes.
- Test checklist.

## 8. Safety rules
- Use JQuiver GridUtil/grid metadata first; do not create Spring Boot Controller, Service, Repository/DAO, DTO, request/response, or Entity classes unless explicitly asked or verified metadata patterns are insufficient.
- When grid row actions open Form Builder screens, copy the URL, button/action, and navigation pattern from an existing verified module. Do not hardcode `/cf/*`; use configured `{view.path}/{router-path}`.
- Do not expose private fields in listing pages.
- Avoid unbounded queries.
- Verify datasource before editing.
- Recommend backup before metadata changes.

## 9. Examples
- HRS includes grids for audit plans, interview candidates, panels, and questions.
- SBI FAC includes job and applicant listing views.

## 10. Things not to do
- Do not assume a grid query is read-only if row actions call APIs.
- Do not load full large tables without pagination.
- Do not forget role access.
- Do not invent `/cf/{path}` unless `view.path: /cf` is configured.
- Do not create grid actions that send users to unverified Form Builder URLs or standalone generic form pages.
- Do not copy grid IDs across instances.
