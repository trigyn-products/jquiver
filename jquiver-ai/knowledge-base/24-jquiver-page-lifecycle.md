# JQuiver Page Lifecycle

## Purpose
Explain how a JQuiver page can be assembled from route, target metadata, templates, forms, grids, APIs, datasources, and permissions.

## When to use this file
Use this file when tracing how a visible page is produced or why a page behaves a certain way.

## Related files
- `03-module-router.md`
- `04-form-builder.md`
- `06-grid-utils.md`
- `07-dynamic-rest-api.md`
- `15-security-users-roles.md`

## Known facts
- Routes are represented in `jq_module_listing`.
- Route target lookup determines whether the target is a dashboard, form, template, REST API, external URL, or another configured target type.
- Forms, grids, templates, APIs, and datasources are connected through metadata IDs.

## Lifecycle model
1. Request arrives for a JQuiver URL.
2. Route metadata is resolved.
3. Security and layout behavior are evaluated.
4. Route target metadata is loaded.
5. The page shell renders.
6. Client-side code may initialize grids, autocompletes, Form.io components, and buttons.
7. Additional data may load through grid endpoints or Dynamic REST APIs.
8. User actions may call save APIs, upload files, navigate to forms, or trigger downloads.

## Page dependency map
For any page, map:
- Route URL.
- Target type.
- Target ID.
- Template/form/grid/dashboard metadata.
- Dynamic REST APIs referenced by page scripts.
- Datasource IDs.
- Resource bundle labels.
- Role/entity permissions.
- File bins.
- Scheduler interactions if any.

## Safe AI-agent usage
- Do not treat rendered HTML as the full source of truth.
- Trace metadata dependencies before proposing a fix.
- Verify public pages for anonymous access and data exposure.
- Avoid invoking save/delete APIs while exploring.

## TODO items to verify
- TODO: verify exact request handling from controller/service source code.
- TODO: verify how permissions are checked during route resolution.
- TODO: verify layout inclusion behavior.
- TODO: verify client-side initialization order.

## Example
SBI `/sbi/view/jdd` loads a job description page shell and fetches data through service-layer APIs. The agent should inspect both the template and the API metadata.

