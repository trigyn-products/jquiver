# Form.io Pluggable Forms

## Purpose
Explain JQuiver Form.io/pluggable form usage for surveys, feedback forms, public forms, and external application embedding.

## When to use this file
Use this file when designing or analyzing Form.io-backed forms, anonymous forms, public surveys, feedback capture, or iframe embeds.

## Related files
- `20-external-form-embedding.md`
- `04-form-builder.md`
- `25-authentication-authorization-flow.md`
- `../playbooks/create-formio-pluggable-form.md`
- `../playbooks/embed-formio-external-app.md`

## Known facts
- ARK examples include iframe embeds for Form.io/pluggable forms.
- `single submit.html` embeds `/arkkt/view/cf`.
- `multi submit.html` embeds `/arkkt/view/cfs`.
- Known technology context includes Form.io integration.
- Observed HRS metadata includes `jq_form_io` and `jq_formio_route`.
- Observed `jq_form_io` columns include `form_io_id`, `form_name`, `form_io_json`, `form_io_type`, `persistence_type`, `multi_submit`, and `route_name`.
- Observed `jq_formio_route` links `form_io_id`, `module_id`, and `multi_submit`.

## Form.io modes
Default Form.io:
- Use when JQuiver owns the Form.io JSON and standard persistence behavior is enough.
- Verify `persistence_type` before assuming where submissions are saved.
- Verify whether captcha/CSRF/public access is needed.

Custom Form.io:
- Use when the Form.io schema needs custom components, custom validation, or non-standard submission handling.
- Treat custom scripts/components as executable behavior.
- Verify the renderer and component support in the running JQuiver version.

Custom pluggable Form.io:
- Use when an external application embeds a JQuiver route through an iframe or equivalent integration.
- Verify `jq_formio_route`, module route, public access, frame headers, and submit mode.
- Use for survey, feedback, public application, or external workflow capture only after explicit access review.

Single submit:
- Allows one submission per intended identity/session/context.
- TODO: verify exact enforcement key from source and metadata.

Multi submit:
- Allows repeated submissions where the workflow expects multiple responses.
- TODO: verify exact multi-submit rules and duplicate behavior from source.

## Conceptual behavior
Form.io/pluggable forms let JQuiver expose form experiences that can be embedded outside the main JQuiver UI. These are useful for survey, feedback, public application, and external-application workflows.

The external application usually does not own the form logic. It embeds a JQuiver route in an iframe, and JQuiver handles the form rendering and submission lifecycle.

## Relationships
- External app -> iframe -> JQuiver route.
- Route -> Form.io/pluggable form metadata.
- Form -> persistence behavior.
- Form -> security/anonymous access rules.
- Form -> optional file upload, captcha, or validation behavior.

## Safe AI-agent usage
- Do not assume anonymous access is allowed.
- Verify frame headers, authentication, and submission behavior.
- Treat public forms as security-sensitive.
- Verify whether one submission or multiple submissions are allowed.
- Verify whether the form is default, custom, or custom pluggable before changing JSON.
- Do not copy custom component JSON into another instance until renderer support is confirmed.
- Avoid collecting or exposing private user data without explicit requirements.

## TODO items to verify
- TODO: verify Form.io metadata tables and lifecycle from JQuiver source/database.
- TODO: verify single-submit and multi-submit semantics.
- TODO: verify public/anonymous access model.
- TODO: verify iframe, CORS, and frame-header behavior.
- TODO: verify `form_io_type` and `persistence_type` lookup values.
- TODO: verify custom component support and version compatibility.

## Example
A customer-facing survey page can embed a JQuiver pluggable form in an iframe. The agent must verify the route, submission mode, and access rules before calling it production-ready.
