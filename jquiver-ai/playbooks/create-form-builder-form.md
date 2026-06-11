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

## Step-by-step implementation approach
1. Find a similar form in the target instance.
2. Verify target table/schema and persistence rules.
3. Design form fields and validation.
4. Prepare select/load query.
5. Prepare save query or service logic.
6. Configure datasource if needed.
7. Create route and permissions.
8. Test create, edit, validation, cancel, and error states.
9. Test with allowed/disallowed roles.

Example read-only form check:

```sql
-- Example only.
SELECT form_id, form_name, datasource_id, is_captcha_enabled, is_csrf_enabled
FROM jq_dynamic_form
WHERE form_name = '<FORM_NAME>';
```

## Validation checklist
- Form loads.
- Required fields validate.
- Save works in non-production.
- Edit mode loads existing data.
- Error messages are clear.
- Datasource correct.
- Permissions correct.
- No sensitive data leaked.

## Common mistakes
- Missing save query.
- Wrong datasource.
- Form field name mismatch.
- Client-side validation without server-side persistence validation.
- Route points to wrong form ID.

## Rollback plan
- Restore previous form and save-query metadata if modifying existing form.
- Remove new route permissions if new form must be hidden.
- Revert test data separately.

## Related skills and reference docs
- `../skills/jquiver-form-builder/SKILL.md`
- `../knowledge-base/04-form-builder.md`
- `../developer-runbook/troubleshoot-forms.md`
- `create-module-route.md`

