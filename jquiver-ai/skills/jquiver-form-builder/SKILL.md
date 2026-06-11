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
- `../../knowledge-base/03-module-router.md`
- `../../knowledge-base/13-additional-datasource.md`
- `../../playbooks/create-form-builder-form.md`
- `../../developer-runbook/troubleshoot-forms.md`
- `../../reference/metadata-table-reference.md`

## 6. Step-by-step workflow
1. Inspect `jq_dynamic_form`.
2. Inspect related `jq_dynamic_form_save_queries`.
3. Inspect route metadata pointing to the form.
4. Verify datasource and query type.
5. Review form body for fields, scripts, validations, resource bundles, and file bins.
6. Verify save behavior and side effects.
7. Test create, edit, validation errors, save success, and permission behavior.
8. Capture rollback metadata before changes.

## 7. Output format
Return:
- Form map.
- Route and datasource summary.
- Load/save query summary.
- Field and validation notes.
- Risks/TODOs.
- Test checklist.

## 8. Safety rules
- Do not write save queries without backup.
- Avoid exposing PII from form data.
- Validate datasource IDs before query changes.
- Treat form scripts as executable UI/server behavior depending on context.

## 9. Examples
- ARK and SBI FAC use metadata-backed forms for business workflows.
- HRS has forms for audit, interview, consultant, L&D, and exit interview modules.

## 10. Things not to do
- Do not edit form body without checking route and save queries.
- Do not assume a form uses the default datasource.
- Do not skip role/API/file permission checks.
- Do not remove validation without approval.
