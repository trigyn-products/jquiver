# Templates

## Purpose
Explain JQuiver templates as metadata-backed rendering assets for pages, fragments, CSS/JS, or reusable UI content.

## When to use this file
Use this file when working with template routes, page shells, UI fragments, custom CSS/JS routes, or server-rendered content.

## Related files
- `03-module-router.md`
- `04-form-builder.md`
- `24-jquiver-page-lifecycle.md`
- `21-jquiver-ui-standards.md`

## Known facts
- Observed template metadata uses `jq_template_master`.
- Template routes appear in route metadata with a Template target.
- Observed HRS `jq_template_master` columns include `template_id`, `template_name`, `template`, `template_type_id`, checksum, and custom update flag.
- HRS contained hundreds of template records.
- Observed Dynamic REST response producers include `application/xml` and `email/xml`.
- SBI FAC public pages include template-backed job listing, job description, OTP, and application flows.
- ARK and SBI exports include custom style/template assets in metadata.

## Template categories
Page templates:
- Render UI pages, fragments, scripts, or style blocks used by routes.
- Often contain FreeMarker-style expressions, resource bundle calls, and JavaScript initialization.

Email XML templates:
- Used when Dynamic REST or mail logic produces `email/xml`.
- Usually combine recipients, subject, body, and merge data.
- Must be reviewed as side-effecting because rendering may be followed by mail behavior through `jq_template_master`, `jq_failed_mail_history`, `jq_mail_history_data`, scheduler, Dynamic REST, or service logic.
- TODO: verify exact XML schema and mail send pipeline from source.

Webclient XML templates:
- Use when JQuiver stores an XML body for HTTP/client integration behavior.
- The exact webclient XML contract is not yet verified from source.
- Treat endpoint URLs, headers, and secrets as sensitive.
- TODO: verify table/type fields and runtime execution path for webclient XML.

Reusable fragments:
- Shared blocks may be included by forms, templates, or service logic.
- Confirm dependencies before editing because one template may be reused by many routes or APIs.

## Template lifecycle
1. Route resolves to a template target.
2. Template metadata is loaded.
3. Template content renders static and dynamic page structure.
4. JavaScript, CSS, grids, forms, APIs, and autocompletes may be referenced by the template.
5. Page behavior may continue through client-side service calls.

## Relationships
- Template -> route.
- Template -> resource bundle keys.
- Template -> Dynamic REST APIs.
- Template -> grid/form elements.
- Template -> CSS/JS dependencies.

## Safe AI-agent usage
- Treat template bodies as executable UI behavior.
- Verify any service URLs referenced in JavaScript.
- Avoid editing templates without understanding dependent forms, grids, and APIs.
- Do not assume a template is only presentational.
- For email/XML templates, do not trigger send flows during analysis.
- For webclient XML templates, redact URLs, headers, credentials, and tokens.
- Preserve checksum/custom-update conventions until source behavior is verified.

## TODO items to verify
- TODO: verify template engine and supported syntax from source code.
- TODO: verify template type IDs and system/custom distinction.
- TODO: verify script and style loading conventions.
- TODO: verify email-XML and webclient-XML schemas.
- TODO: verify how template checksum is computed and enforced.

## Example
A public job listing template may render filter controls, initialize a grid, and call service-layer APIs to open job details or OTP validation pages.
