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

## Step-by-step implementation approach
1. Read the KB files for router, form builder, grid utils, security, and page lifecycle.
2. Identify a similar working module in the target instance.
3. Design or verify the business table and listing view.
4. Create the grid definition for list/search behavior.
5. Create the form definition and save behavior.
6. Create the route/menu metadata.
7. Add localized route/resource labels if required.
8. Add role/entity access.
9. Add file bin configuration only if the form needs uploads.
10. Add Dynamic REST APIs only when metadata form/grid behavior is not enough.
11. Test list, create, edit, validation, permissions, and rollback.

Example read-only dependency check:

```sql
-- Example only. Verify exact table names first.
SELECT module_id, module_url, target_lookup_id, target_type_id
FROM jq_module_listing
WHERE module_url = '<ROUTE_URL>';
```

## Validation checklist
- Business table/view exists.
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

