# Module Router

## Purpose
Explain how JQuiver routes and menu entries connect URLs to metadata-backed targets.

## When to use this file
Use this file when working with menus, routes, home pages, page targets, embedded pages, or external links.

## Related files
- `24-jquiver-page-lifecycle.md`
- `15-security-users-roles.md`
- `../playbooks/create-module-route.md`
- `../reference/metadata-table-reference.md`

## Known facts
- Observed route metadata uses `jq_module_listing`.
- Observed localized route names use `jq_module_listing_i18n`.
- Observed target lookup descriptions include Dashboard, Form Builder, REST API, Model and View, Template, Root, and Target URL.
- Routes can represent internal pages or external target URLs.
- Observed HRS route metadata includes `is_home_page`, `include_layout`, `is_inside_menu`, `open_in_new_tab`, `header_json`, and `request_param_json`.
- Before creating router/menu/grid navigation links, read `application.yml` or `application.yaml` and determine configured `view.path`; if not configured, use `/view`.
- Router-created JQuiver pages must link using `{view.path}/{router-path}`. Do not invent `/cf/*` or other prefixes unless configured in `application.yml` or verified from an existing working module.
- Any user-facing screen created via JQuiver must have a valid router/module entry and menu linkage. Do not create only a template or GridUtil row and consider the screen complete.

## Route lifecycle
1. User opens a URL or selects a menu item.
2. JQuiver resolves route metadata.
3. Route target type determines what the runtime should render or execute.
4. Target metadata is loaded, such as a form, template, dashboard, or grid-backed page.
5. Security and role/entity permission checks may apply.
6. The final page may load additional data through grids, APIs, autocompletes, or file endpoints.

## Important relationships
- Route -> target lookup/type.
- Route -> target metadata ID.
- Route -> localized display name.
- Route -> role/entity permission association.
- Route -> request parameters and layout behavior.

## Define home page
- Use the home-page flag only after confirming there is no existing active home page for the same context.
- Verify whether home page is global, role-specific, user-specific, or context-specific in the target JQuiver version.
- Confirm the target route is accessible to all users who will land on it.
- Ensure the home page target does not depend on missing query parameters.
- Do not make a side-effecting REST route the home page.
- TODO: verify exact `is_home_page` resolution logic from source.

## Safe AI-agent usage
- Do not assume a route is safe to expose publicly.
- Check if a route is marked as home page or included in menu.
- Verify route target type before modifying target metadata.
- Check role access before changing menu visibility.
- For grid View/Edit/Open links, identify target router path from metadata and build `{view.path}/{router-path}`; copy row-parameter patterns only from an existing verified module.
- Add role/entity access metadata for new user-facing routes according to the existing RBAC pattern.

## TODO items to verify
- TODO: verify all route target lookup IDs from current source/database.
- TODO: verify menu rendering behavior from source code.
- TODO: verify layout inclusion behavior.
- TODO: verify route parameter handling and URL conflict rules.

## Example
In SBI FAC, public routes include job-listing and application pages. The running `/sbi/` URL returned a public careers page, but exact route permissions still require database/source verification.
