---
name: jquiver-formio
description: Use for JQuiver Form.io and pluggable form analysis, creation, embedding, and troubleshooting involving jq_form_io, jq_formio_route, default/custom/custom pluggable modes, single-submit, multi-submit, iframe embedding, and public access.
---

# JQuiver Form.io

## 1. Skill name
JQuiver Form.io

## 2. Purpose
Guide an agent through safe handling of JQuiver Form.io-based forms, pluggable forms, surveys, feedback forms, and external embeds.

## 3. Trigger phrases / when to use
- "Form.io"
- "pluggable form"
- "custom pluggable"
- "single submit"
- "multi submit"
- "embed form"
- "external survey"

## 4. Required context
- Form.io form name/ID.
- Mode: default, custom, or custom pluggable.
- Submit mode: single or multi.
- Target route and embed host.
- Persistence requirement.
- Public/authenticated access requirement.

## 5. Files to read first
- `../../knowledge-base/05-formio-pluggable-forms.md`
- `../../knowledge-base/20-external-form-embedding.md`
- `../../playbooks/create-formio-pluggable-form.md`
- `../../playbooks/embed-formio-external-app.md`
- `../../examples/formio-embed-single-submit.html`
- `../../examples/formio-embed-multi-submit.html`

## 6. Step-by-step workflow
1. Inspect `jq_form_io` and `jq_formio_route` if present.
2. Identify mode and submit behavior.
3. Inspect route metadata and access rules.
4. Review Form.io JSON for fields, validation, custom components, and file upload.
5. Verify persistence behavior.
6. Test rendering directly in JQuiver.
7. Test iframe embedding from the external host.
8. Test duplicate submit and validation behavior.
9. Review frame/CORS/security constraints.

## 7. Output format
Return:
- Form.io form map.
- Mode and submit behavior.
- Route/embed details.
- Persistence and security notes.
- Risks/TODOs.
- Test checklist.

## 8. Safety rules
- Do not assume anonymous access is allowed.
- Treat public forms as security-sensitive.
- Do not copy custom components unless renderer support is verified.
- Avoid collecting private data without retention/access rules.

## 9. Examples
- ARK has single-submit and multi-submit external embed HTML examples.
- HRS has `jq_form_io` metadata for a Form.io form.

## 10. Things not to do
- Do not confuse Form Builder forms with Form.io forms.
- Do not publish iframe embeds before access review.
- Do not skip duplicate-submit testing.
- Do not expose private submission data.
