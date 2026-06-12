# Troubleshoot Forms

## Purpose
Troubleshoot JQuiver Form Builder form load, render, validation, and save issues.

## Prerequisites
- Target form name or route identified.
- Read access to form metadata.
- Permission to inspect logs and related tables.
- Backup available before any metadata edits.

## Inputs required
- Form name or ID: `<FORM_ID>`.
- Route URL: `<ROUTE_URL>`.
- Symptom: `<SYMPTOM>`.
- User role/security context.
- Related datasource ID, if known.
- Error log excerpt with sensitive values redacted.

## Steps
1. Identify the route that opens the form.
2. Verify route target points to the expected form.
3. Inspect `jq_dynamic_form` metadata.
4. Inspect form select query and body.
5. Inspect `jq_dynamic_form_save_queries`.
6. Verify datasource ID and target schema.
7. Verify any file bin used by the form.
8. Verify Dynamic REST APIs called by form scripts.
9. Check captcha/CSRF flags if submission fails.
10. Reproduce in non-production when possible.
11. Compare the form body against a verified working Form Builder example from the same instance or closest matching module.
12. Verify button alignment/container, save/cancel/back handlers, required-field highlighting, message behavior, and `view.path`/`api.path` URL prefixes.
13. Use read-only SQL/logs before editing metadata.
14. If a change is required, follow the form playbook and recommend backup.

## Validation checklist
- Route resolves to correct form.
- Form metadata exists.
- Select query works.
- Save query works in safe test.
- Datasource valid.
- Permissions valid.
- File bins valid if used.
- Client-side API calls verified.
- Buttons, validation, messages, field highlighting, and navigation match a verified JQuiver form pattern.
- No hardcoded `/cf/*` URLs exist unless configured or verified.
- Only the correct save query is attached.

## Common errors
- Missing or broken save query.
- Query targets wrong schema.
- Form body references missing API or field.
- Captcha/CSRF mismatch.
- Role lacks access to route or target metadata.
- JavaScript error prevents submit.
- Generic custom Form Builder HTML that does not follow verified JQuiver button, validation, message, or navigation patterns.
- Direct `/cf/*` links when `view.path`/`api.path` were not checked.

## Rollback/safety notes
- Back up `jq_dynamic_form` and save query rows before edits.
- Keep previous form body and query checksums/values if available.
- Avoid production form edits without tested rollback.
- If submission created bad data, rollback data separately and carefully.

## Related KB/reference/playbook files
- `../knowledge-base/04-form-builder.md`
- `../knowledge-base/24-jquiver-page-lifecycle.md`
- `../playbooks/create-form-builder-form.md`
- `../playbooks/modify-existing-module.md`
- `jquiver-metadata-navigation.md`
