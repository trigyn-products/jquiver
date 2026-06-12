---
name: jquiver-form-builder
description: Use for JQuiver Form Builder analysis, creation, and troubleshooting involving jq_dynamic_form, jq_dynamic_form_save_queries, form body templates, load/save queries, validation, captcha/CSRF, datasources, and route integration.
---

# JQuiver Form Builder

## 1. Skill name
JQuiver Form Builder

## 2. Purpose
Guide an agent through safe creation, modification, or troubleshooting of JQuiver Form Builder forms.

## 3. Trigger phrases / when to use
- "Form Builder"
- "dynamic form"
- "jq_dynamic_form"
- "form save query"
- "form not loading"
- "captcha"
- "CSRF"

## 4. Required context
- Form name/ID.
- Target route.
- Load query and save requirements.
- Backing table/view.
- Datasource ID, if any.
- Validation, captcha, CSRF, and file upload requirements.

## 5. Files to read first
- `../../knowledge-base/04-form-builder.md`
- `../../knowledge-base/21-jquiver-ui-standards.md`
- `../../knowledge-base/03-module-router.md`
- `../../knowledge-base/13-additional-datasource.md`
- `../../reference/ui-standards.md`
- `../../reference/environment-config-reference.md`
- `../../playbooks/create-form-builder-form.md`
- `../../developer-runbook/troubleshoot-forms.md`
- `../../reference/metadata-table-reference.md`
- One existing verified Form Builder example from the same instance or closest matching module.

## 6. Step-by-step workflow
1. Inspect at least one existing verified Form Builder example from the same instance or closest matching module.
2. Inspect `jq_dynamic_form`.
3. Inspect related `jq_dynamic_form_save_queries`.
4. Inspect route metadata pointing to the form.
5. Verify datasource and query type.
6. Select only the save query verified for the target form/module; if multiple exist, compare form ID, module, table, context, and purpose before choosing.
7. Read `application.yml` or `application.yaml` before creating route/API links; use `{view.path}/{router-path}` and `{api.path}/{api-path}` with defaults `/view` and `/api`.
8. Review form body for fields, scripts, validations, resource bundles, and file bins.
9. Copy and adapt the verified form's button layout, validation style, message handling, field highlighting, script functions, and navigation pattern.
10. Verify save behavior and side effects.
11. Test create, edit, validation errors, save success, cancel/back navigation, and permission behavior.
12. Capture rollback metadata before changes.

## 7. Output format
Return:
- Form map.
- Route and datasource summary.
- Load/save query summary.
- Field and validation notes.
- Existing form/example used as reference.
- Configured `view.path` and `api.path`.
- Selected save query and why.
- Navigation URL pattern used.
- Validation/message pattern reused.
- Risks/TODOs.
- Test checklist.

## 8. Safety rules
- Use JQuiver Form Builder metadata first; do not create Spring Boot Controller, Service, Repository/DAO, DTO, request/response, or Entity classes unless explicitly asked or verified metadata patterns are insufficient.
- Do not write save queries without backup.
- Do not attach multiple or unrelated save queries unless an existing verified form pattern requires it.
- Avoid exposing PII from form data.
- Validate datasource IDs before query changes.
- Treat form scripts as executable UI/server behavior depending on context.
- Do not generate a generic Bootstrap/jQuery form unless explicitly asked.
- Do not invent custom Save/Cancel button HTML or onclick handlers when existing Form Builder examples provide a standard save/cancel/back pattern.
- Required-field validation must follow existing JQuiver examples, including field highlighting, focus behavior, required messages, alert/message containers, and success/error display.
- Do not hardcode `contextPath + '/cf/...'`; use configured `view.path`/`api.path` or copy an exact verified pattern.

## Hard failure conditions
Reject or revise the generated form if:
- Buttons are left-aligned without verified precedent.
- Save/cancel buttons use custom generic handlers instead of the existing JQuiver pattern.
- Validation does not highlight required fields as existing examples do.
- URLs are hardcoded as `/cf/*`.
- More than one save query is attached without verified reason.
- `application.yml`/`application.yaml` was not checked for `view.path` and `api.path`.

## Validation checklist
- Did I inspect an existing verified JQuiver form?
- Did I reuse the standard button container?
- Are buttons aligned as per existing JQuiver form examples?
- Is the secondary/cancel/back button placed correctly?
- Did I avoid custom generic Save/Cancel handlers?
- Did I use existing validation/message/highlight pattern?
- Are required fields highlighted/focused as per existing pattern?
- Did I avoid hardcoded `/cf` links?
- Did I read `application.yml`/`application.yaml` for `view.path` and `api.path`?
- Did I use `{view.path}/{router-path}` for navigation?
- Did I use `{api.path}/{api-path}` for API/save calls?
- Did I select only the correct save query?
- Did I avoid unrelated duplicate save queries?
- Did I check a similar module before finalizing?

## 9. Examples
- ARK and SBI FAC use metadata-backed forms for business workflows.
- HRS has forms for audit, interview, consultant, L&D, and exit interview modules.

## 10. Things not to do
- Do not edit form body without checking route and save queries.
- Do not assume a form uses the default datasource.
- Do not invent `/cf/*` links unless configured or verified.
- Do not generate forms with left-aligned custom buttons, custom showMessage-only validation, `contextPath + '/cf/sdf'`, or `contextPath + '/cf/{module}'` unless this exact pattern is verified in the current instance.
- Do not skip role/API/file permission checks.
- Do not remove validation without approval.
