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
- Verify query type meanings before writing metadata.
- Recommend backup before changing form rows or save queries.
- Treat embedded JavaScript and SQL as executable behavior.
- Identify whether a form is public or authenticated before suggesting changes.
- Avoid exposing form data that contains PII.

## TODO items to verify
- TODO: verify exact form lifecycle and supported query types from source code.
- TODO: verify validation and error-handling conventions.
- TODO: verify revision/versioning behavior for forms.
- TODO: verify Form Builder UI conventions from source.

## Example
For an applicant form, inspect the route, form metadata, save queries, file upload bins, captcha settings, and any Dynamic REST APIs used by client-side scripts.

