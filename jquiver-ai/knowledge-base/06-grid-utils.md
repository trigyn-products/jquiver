# Grid Utils

## Purpose
Explain JQuiver grid/listing metadata and safe usage patterns.

## When to use this file
Use this file when creating, modifying, or troubleshooting list pages, pqGrid-backed pages, searchable grids, or reporting views.

## Related files
- `02-core-database-model.md`
- `13-additional-datasource.md`
- `29-performance-guidelines.md`
- `../playbooks/create-grid.md`
- `../developer-runbook/troubleshoot-grids.md`

## Known facts
- Observed grid metadata uses `jq_grid_details`.
- Grid metadata includes grid name, table/view name, column names, query type, datasource ID, and custom filter criteria.
- Known frontend context includes pqGrid.
- SBI FAC uses `jd_detailsGrid` over `view_jd_details` and `jd-applicantsGrid` over `view_jd_applicants`.
- ARK uses grids such as `ark-policy-detailsGrid` and `ManufacturersGrid`.

## Grid lifecycle
1. A route or template points to a grid.
2. Grid metadata identifies a table or view.
3. JQuiver executes a grid data request, often with paging/filter/sort inputs.
4. The frontend renders rows using pqGrid or related grid utilities.
5. Actions may link to forms, APIs, downloads, or detail pages.

## Relationships
- Grid -> table or view.
- Grid -> datasource ID when data is outside the default schema.
- Grid -> route/template page.
- Grid -> permissions and actions.
- Grid -> custom filter criteria.

## Safe AI-agent usage
- Use JQuiver grid metadata first; do not create Spring Boot Controller, Service, Repository/DAO, DTO, request/response, or Entity classes unless explicitly asked or verified metadata patterns are insufficient.
- Verify the backing table/view exists.
- Check whether the grid uses the default schema or an additional datasource.
- Avoid selecting all columns from large tables unless needed.
- Review filters and indexes for performance.
- Do not assume a grid is read-only; row actions may trigger changes.
- Do not treat a grid/list page as complete without router/module, menu if user-facing, role/entity access, and resource labels when required.

## TODO items to verify
- TODO: verify grid pagination, filtering, sorting, and permission behavior from source code.
- TODO: verify supported query types.
- TODO: verify pqGrid integration details and response payload format.

## Example
If a grid shows no data, inspect `jq_grid_details`, the backing view, datasource ID, role access, and the service request used to fetch paginated rows.
