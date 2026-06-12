# Create Form Builder Form

## Goal
Create a JQuiver Form Builder form with safe load, validation, and save behavior.

## When to use
Use this playbook when adding a metadata-driven form for create/edit/detail workflows.

## Required inputs
- Form name.
- Form purpose.
- Target table or API.
- Field list and validation rules.
- Select/load query requirements.
- Save/update behavior.
- Datasource ID if custom schema.
- Route/menu requirements.

## Files/tables/configuration to inspect first
- `jq_dynamic_form`.
- `jq_dynamic_form_save_queries`.
- `jq_module_listing`.
- `jq_additional_datasource`.
- Related business table/view.
- Similar working form metadata.
- `application.yml` or `application.yaml` for configured `view.path`.
- `knowledge-base/21-jquiver-ui-standards.md`.
- `reference/ui-standards.md`.
- `reference/environment-config-reference.md`.
- One existing verified Form Builder form from the same instance or closest matching module.

## Step-by-step implementation approach
1. Find a similar form in the target instance.
2. Verify its button container, alignment, validation/message/highlight behavior, save/cancel handlers, and navigation pattern.
3. Verify target table/schema and persistence rules.
4. Use Form Builder metadata; do not create Spring Boot CRUD classes unless explicitly asked.
5. Design form fields and validation by adapting the verified pattern.
6. Prepare select/load query.
7. Identify the exact save query linked to the target form/module; if multiple exist, compare form ID/module/table/context/purpose.
8. Prepare only the verified save query or service logic.
9. Read `application.yml` or `application.yaml`; create route links as `{view.path}/{router-path}` and API/save calls as `{api.path}/{api-path}`, defaulting to `/view` and `/api`.
10. Configure datasource if needed.
11. Create route and permissions.
12. Test create, edit, validation, cancel/back, and error states.
13. Test with allowed/disallowed roles.

Example read-only form check:

```sql
-- Example only.
SELECT form_id, form_name, datasource_id, is_captcha_enabled, is_csrf_enabled
FROM jq_dynamic_form
WHERE form_name = '<FORM_NAME>';
```

## Validation checklist
- Form loads.
- Only the correct save query is selected.
- Duplicate/unrelated save queries are avoided.
- Route/menu link uses configured `view.path` or default `/view`.
- Existing verified form example was inspected.
- Standard button container and alignment were reused.
- Secondary/cancel/back button placement matches the verified pattern.
- Custom generic Save/Cancel handlers were avoided.
- Required fields highlight/focus as the verified pattern does.
- Validation/message behavior reuses the verified pattern.
- Hardcoded `/cf` links were avoided.
- `application.yml`/`application.yaml` was checked for `view.path` and `api.path`.
- Navigation uses `{view.path}/{router-path}` and API/save calls use `{api.path}/{api-path}`.
- Required fields validate.
- Save works in non-production.
- Edit mode loads existing data.
- Error messages are clear.
- Datasource correct.
- Permissions correct.
- No sensitive data leaked.

## Common mistakes
- Missing save query.
- Selecting multiple save queries without an existing verified form pattern.
- Attaching unrelated save queries to a new form.
- Creating Spring Boot Controller/Service/Repository/DTO/Entity classes instead of Form Builder metadata.
- Wrong datasource.
- Form field name mismatch.
- Client-side validation without server-side persistence validation.
- Route points to wrong form ID.
- Generating a generic Bootstrap/jQuery form instead of copying a verified JQuiver Form Builder pattern.
- Left-aligning custom buttons, inventing showMessage-only validation, or using `contextPath + '/cf/...'` without verified precedent.

## Rollback plan
- Restore previous form and save-query metadata if modifying existing form.
- Remove new route permissions if new form must be hidden.
- Revert test data separately.

## Related skills and reference docs
- `../skills/jquiver-form-builder/SKILL.md`
- `../knowledge-base/04-form-builder.md`
- `../developer-runbook/troubleshoot-forms.md`
- `create-module-route.md`
