# Form Builder

## Purpose
Explain JQuiver metadata-driven dynamic forms and how agents should analyze them safely.

## When to use this file
Use this file when creating, modifying, or troubleshooting a JQuiver Form Builder form.

## Related files
- `05-formio-pluggable-forms.md`
- `24-jquiver-page-lifecycle.md`
- `13-additional-datasource.md`
- `../playbooks/create-form-builder-form.md`
- `../developer-runbook/troubleshoot-forms.md`

## Known facts
- Observed form metadata uses `jq_dynamic_form`.
- Observed form save query metadata uses `jq_dynamic_form_save_queries`.
- Form metadata can include select logic, form body markup, form type, datasource ID, query type, captcha flag, and CSRF flag.
- SBI FAC includes forms such as `job_details`, `applicant_details`, `intrv-candidate-details-form`, and `intrv_assessment_form`.
- ARK includes forms for policies, exceptions, hospitals, email templates, manufacturers, and policy tests.

## Form lifecycle
1. A route points to a form target.
2. JQuiver loads form metadata.
3. Form select logic prepares initial data.
4. Form body renders HTML/template/JavaScript UI.
5. User submits data.
6. Save query or service logic persists data.
7. The form may redirect, refresh, show validation messages, upload files, or call Dynamic REST APIs.

## Key relationships
- Route -> form ID.
- Form -> select query and body.
- Form -> save query rows.
- Form -> datasource ID if it queries an additional database.
- Form -> file bin if the UI includes upload behavior.
- Form -> security and role permissions through route/entity metadata.

## Safe AI-agent usage
- Use JQuiver Form Builder metadata first; do not create Spring Boot Controller, Service, Repository/DAO, DTO, request/response, or Entity classes unless explicitly asked or verified metadata patterns are insufficient.
- Verify query type meanings before writing metadata.
- Recommend backup before changing form rows or save queries.
- Do not attach multiple or unrelated save queries; select the save query verified for the target form/module.
- Treat embedded JavaScript and SQL as executable behavior.
- Identify whether a form is public or authenticated before suggesting changes.
- Avoid exposing form data that contains PII.

## Verified-example-first rule
- Before generating or modifying a Form Builder HTML body, inspect at least one existing verified Form Builder example from the same instance or closest matching module.
- Reuse the verified example's button layout, validation style, message handling, field highlighting, script functions, save behavior, and navigation pattern.
- Do not invent custom Save/Cancel button HTML, generic onclick handlers, showMessage-only validation, or standalone Bootstrap/jQuery form structure when verified JQuiver examples exist.
- Form action buttons must use the standard action/button container from existing examples. Do not left-align buttons unless an existing verified form does so.
- Before generating save, cancel/back, grid action, or router URLs, read `application.yml`/`application.yaml`; use `{view.path}/{router-path}` and `{api.path}/{api-path}`. Do not hardcode `/cf/*` unless configured or verified.
- If multiple save queries exist, compare form ID, module, table, and purpose before choosing. If uncertain, mark TODO instead of wiring unrelated save queries.

## Form Builder body standard
A generated body should follow verified JQuiver conventions for container structure, top band/header, field layout, required markers, date fields, dropdown population, validation, message display, button alignment, save/cancel behavior, router navigation, and script imports.

Anti-pattern: left-aligned custom buttons, custom showMessage-only validation, `contextPath + '/cf/sdf'`, or `contextPath + '/cf/{module}'` unless that exact pattern is verified in the current instance.

## TODO items to verify
- TODO: verify exact form lifecycle and supported query types from source code.
- TODO: verify validation and error-handling conventions.
- TODO: verify revision/versioning behavior for forms.
- TODO: verify Form Builder UI conventions from source.

## Example
For an applicant form, inspect the route, form metadata, save queries, file upload bins, captcha settings, and any Dynamic REST APIs used by client-side scripts.
