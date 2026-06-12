# Create End-to-End CRUD Module

## Goal
Create a complete JQuiver CRUD module using verified metadata patterns.

## When to use
Use this playbook when a new feature needs a list page, create/edit form, route/menu entry, permissions, optional APIs, optional file upload, and optional multilingual labels.

## Required inputs
- Module name: `<MODULE_NAME>`.
- Business table/view design.
- CRUD fields and validation rules.
- Roles that can access the module.
- Datasource: default or additional datasource.
- File upload requirements, if any.
- Route/menu placement.

## Files/tables/configuration to inspect first
- Existing similar CRUD module metadata.
- Business schema table/view.
- `jq_module_listing`, `jq_module_listing_i18n`.
- `jq_grid_details`.
- `jq_dynamic_form`, `jq_dynamic_form_save_queries`.
- `jq_entity_role_association`, `jq_role`.
- `jq_additional_datasource` if custom schema is used.
- `jq_file_upload_config` if uploads are needed.
- `application.yml` or `application.yaml` for `view.path` and `api.path`.
- One existing verified Form Builder form from the same instance or closest matching module before generating create/edit form HTML.

## Step-by-step implementation approach
1. Read the KB files for router, form builder, grid utils, security, and page lifecycle.
2. Identify a similar working module in the target instance.
3. Use JQuiver metadata/configuration patterns; do not create Spring Boot Controller, Service, Repository/DAO, DTO, request/response, or Entity classes unless explicitly asked.
4. Read `application.yml` or `application.yaml`; determine `view.path` and `api.path`, defaulting to `/view` and `/api`.
5. Design or verify the business table and listing view.
6. Create the grid definition for list/search behavior.
7. Before generating create/edit form HTML, inspect and adapt a verified Form Builder example for buttons, validation, messages, field highlighting, save behavior, and navigation.
8. Create the form definition and select only the correct save query for that form/module.
9. Create the route/menu metadata and links as `{view.path}/{router-path}`.
10. Add localized route/resource labels if required.
11. Add role/entity access.
12. Add file bin configuration only if the form needs uploads.
13. Add Dynamic REST APIs only when metadata form/grid behavior is not enough; links use `{api.path}/{api-path}`.
14. Test list, create, edit, validation, permissions, and rollback.

Example read-only dependency check:

```sql
-- Example only. Verify exact table names first.
SELECT module_id, module_url, target_lookup_id, target_type_id
FROM jq_module_listing
WHERE module_url = '<ROUTE_URL>';
```

## Validation checklist
- Business table/view exists.
- Did I read `application.yml` or `application.yaml`?
- What is the configured `view.path`?
- What is the configured `api.path`?
- If not configured, did I default router links to `/view/{path}`?
- If not configured, did I default API links to `/api/{path}`?
- Does the menu/router link use `{view.path}/{router-path}`?
- Is `{router-path}` exactly the router path from metadata?
- Are grid View/Edit/Open links using the same configured router prefix?
- Was an existing verified Form Builder form inspected before generating create/edit HTML?
- Did the form reuse verified buttons, validation, messages, field highlighting, save behavior, and navigation?
- Were generic Form Builder buttons, generic validation, duplicate save queries, and hardcoded `/cf/*` avoided?
- Was only the correct save query selected for the form?
- Were duplicate/unrelated save queries avoided?
- Was an existing similar module checked before creating links?
- Route resolves.
- Grid loads and paginates.
- Create form loads.
- Save/update works in non-production.
- Permissions work for allowed and disallowed roles.
- Resource labels render.
- File upload works if configured.
- No sensitive data appears in logs or UI.

## Common mistakes
- Creating a route without permissions.
- Creating a grid before the backing view exists.
- Creating a form without a rollback copy of save queries.
- Selecting multiple or unrelated save queries for a form.
- Inventing `/cf/*` links when `view.path` is not configured as `/cf`.
- Creating Spring Boot CRUD classes instead of JQuiver metadata.
- Forgetting custom datasource migration.
- Hard-coding labels instead of resource bundle entries.

## Rollback plan
- Restore metadata backup for route, grid, form, permissions, labels, APIs, and file bins.
- Restore custom schema changes from backup if needed.
- Remove menu access or disable route if rollback must be partial.
- Keep data rollback separate from metadata rollback.

## Related skills and reference docs
- `../skills/jquiver-instance-analyzer/SKILL.md`
- `../skills/jquiver-form-builder/SKILL.md`
- `../skills/jquiver-grid-utils/SKILL.md`
- `../knowledge-base/24-jquiver-page-lifecycle.md`
- `../reference/metadata-table-reference.md`
