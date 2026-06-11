# Embed Form.io in External App

## Goal
Embed a JQuiver pluggable Form.io-style form in an external application.

## When to use
Use this playbook for survey, feedback, application, or public forms hosted by JQuiver and displayed inside another app.

## Required inputs
- JQuiver form route URL.
- Submit mode.
- External host page/application.
- Iframe dimensions.
- Authentication/anonymous access decision.
- Branding and user experience requirements.

## Files/tables/configuration to inspect first
- JQuiver route metadata.
- Form.io/pluggable form metadata.
- Security configuration.
- Frame/CORS/header behavior.
- Existing examples in `examples/formio-embed-single-submit.html` and `examples/formio-embed-multi-submit.html`.

## Step-by-step implementation approach
1. Verify the JQuiver form route works directly.
2. Verify access mode and permissions.
3. Verify the route can be framed by the external app.
4. Create an iframe using the approved route URL.
5. Use environment placeholders rather than hard-coded production URLs in reusable docs.
6. Test initial load, validation, submit, success/failure message, and reload behavior.
7. Test browser console for frame/security errors.
8. Verify responsive layout in the external page.

Example iframe pattern:

```html
<!-- Example only. Replace placeholders for the target environment. -->
<iframe
  id="formFrame"
  title="Pluggable Form"
  src="<JQUIVER_BASE_URL>/<CONTEXT_PATH>/view/<FORM_ROUTE>"
  style="width:100%;height:600px;border:1px solid #ccc;">
</iframe>
```

## Validation checklist
- Direct route loads.
- Iframe route loads.
- Submit works.
- Security headers allow expected behavior.
- No mixed-content errors.
- No sensitive URL parameters exposed.

## Common mistakes
- Hard-coding environment URLs.
- Embedding a secured route without login flow.
- Ignoring frame headers.
- Not testing submit behavior inside iframe.
- Too-small iframe height causing unusable form.

## Rollback plan
- Remove iframe from external app.
- Disable or restrict JQuiver route if exposed incorrectly.
- Restore previous host page.
- Preserve submitted data unless approved cleanup is needed.

## Related skills and reference docs
- `../skills/jquiver-formio/SKILL.md`
- `../knowledge-base/20-external-form-embedding.md`
- `create-formio-pluggable-form.md`
- `../examples/formio-embed-single-submit.html`

