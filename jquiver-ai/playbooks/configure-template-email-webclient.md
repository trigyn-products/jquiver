# Configure Template Email And Webclient XML

## Goal
Configure or modify JQuiver templates, including page templates, email/XML templates, and webclient XML templates.

## When to use
Use this playbook when changing reusable templates, email payload templates, external-client XML payloads, or route-backed UI templates.

## Required inputs
- Template name/ID.
- Template category.
- Target route/API/mail flow.
- Merge variables and expected sample data.
- Security and privacy requirements.

## Files/tables/configuration to inspect first
- `jq_template_master`.
- `jq_dynamic_rest_details` if the template is API-driven.
- `jq_response_producer_details`.
- Mail-related metadata if email/XML is used.
- `knowledge-base/08-templates.md`.
- `knowledge-base/17-notifications-mail.md`.

## Step-by-step implementation approach
1. Identify the template and all consumers.
2. Determine whether it is page, fragment, email/XML, or webclient XML.
3. Back up the template body and checksum.
4. Validate merge variables against sample data.
5. Make the smallest template change possible.
6. Render/test in non-production.
7. For email/XML, send only to a test mailbox.
8. For webclient XML, use a test endpoint or dry-run process if available.
9. Review logs and output escaping.

## Validation checklist
- Template renders without errors.
- All merge variables resolve or have safe defaults.
- Output type matches the route/API response producer.
- Email/XML does not leak private data.
- Webclient XML does not expose secrets.
- Consumers still work.

## Common mistakes
- Editing a shared template without listing consumers.
- Triggering real email or external calls during template testing.
- Breaking escaping in XML/HTML output.
- Ignoring checksum/custom-update behavior.

## Rollback plan
- Restore previous template body/checksum.
- Disable related scheduler/API if the template causes production errors.
- Re-test all known consumers.

## Related skills and reference docs
- `../knowledge-base/08-templates.md`
- `../knowledge-base/17-notifications-mail.md`
- `../knowledge-base/07-dynamic-rest-api.md`
- `../reference/metadata-table-reference.md`
