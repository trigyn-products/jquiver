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
- Router/page URLs use configured `view.path` from `application.yml` or `application.yaml`, defaulting to `/view`; API URLs use configured `api.path`, defaulting to `/api`.
- Build page links as `{view.path}/{router-path}` and REST links as `{api.path}/{api-path}`.
- Do not treat a JQuiver screen as complete when only a template, grid, or form exists.

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
- Configured `view.path` and `api.path` before creating menu, grid action, or API links.
- Role/entity access metadata.

Complete list/grid screen chain:
- Query / Dynamic REST / DAO query metadata.
- GridUtil / grid configuration.
- Template, if required.
- Router / module listing entry.
- Menu entry, if user-facing.
- Role/entity access entry.
- Resource bundle / i18n labels, if required.

Complete create/edit form screen chain:
- Dynamic form configuration.
- Insert and update save-query metadata.
- Template, if required.
- Router / module listing entry.
- Link/action from list screen.
- Menu entry, if required by JQuiver pattern.
- Role/entity access entry.
- Resource bundle / i18n labels, if required.
- Role/entity permissions.
- File bins.
- Scheduler interactions if any.

## Safe AI-agent usage
- Do not treat rendered HTML as the full source of truth.
- Trace metadata dependencies before proposing a fix.
- Verify public pages for anonymous access and data exposure.
- Avoid invoking save/delete APIs while exploring.

## Form Builder screen rule
- For Form Builder screens, inspect a verified existing form before generating HTML. Reuse its button container, validation/message/highlight behavior, save query pattern, cancel/back navigation, and script functions.
- A form screen is incomplete if it hardcodes `/cf/*`, omits `application.yml`/`application.yaml` prefix verification, or attaches duplicate save queries without verified reason.

## TODO items to verify
- TODO: verify exact request handling from controller/service source code.
- TODO: verify how permissions are checked during route resolution.
- TODO: verify layout inclusion behavior.
- TODO: verify client-side initialization order.

## Example
SBI `/sbi/view/jdd` loads a job description page shell and fetches data through service-layer APIs. The agent should inspect both the template and the API metadata.
