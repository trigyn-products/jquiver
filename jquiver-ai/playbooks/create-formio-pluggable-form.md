# Create Form.io Pluggable Form

## Goal
Create a JQuiver Form.io/pluggable form for survey, feedback, public, or embedded use.

## When to use
Use this playbook when the form should be embedded in an external application or managed through Form.io-style configuration.

## Required inputs
- Form name.
- Form.io mode: default, custom, or custom pluggable.
- Submit mode: single or multi.
- Target audience: anonymous or authenticated.
- Fields and validation.
- Persistence requirements.
- Embed host/application.
- Security requirements.

## Files/tables/configuration to inspect first
- Existing Form.io/pluggable form metadata.
- `jq_form_io` and `jq_formio_route`, if present in the target version.
- Route metadata.
- Persistence table or form data storage.
- Security/anonymous access configuration.
- Existing embed examples.

## Step-by-step implementation approach
1. Verify Form.io metadata tables and target version.
2. Find a similar working pluggable form.
3. Choose default, custom, or custom pluggable mode.
4. Define fields and validation.
5. Define persistence behavior.
6. Configure route for single-submit or multi-submit behavior.
7. Configure security and anonymous access.
8. Test in JQuiver directly.
9. Test inside iframe embed.
10. Verify successful submit and duplicate submit behavior.

## Validation checklist
- Form renders.
- Form.io mode verified.
- Validation works.
- Submit mode verified.
- Data persists correctly.
- Anonymous/authenticated behavior correct.
- Iframe embed works.
- Sensitive data handling reviewed.

## Common mistakes
- Assuming public access without security verification.
- Confusing single-submit and multi-submit routes.
- Missing frame/header checks.
- Not testing embedded behavior.
- Storing private data without retention rules.
- Copying custom components into an instance where renderer support is not verified.

## Rollback plan
- Disable route or remove public access if form must be withdrawn.
- Restore previous form metadata.
- Preserve submitted data unless explicit approved deletion is required.
- Revert embed host changes separately.

## Related skills and reference docs
- `../skills/jquiver-formio/SKILL.md`
- `../knowledge-base/05-formio-pluggable-forms.md`
- `../knowledge-base/20-external-form-embedding.md`
- `embed-formio-external-app.md`
