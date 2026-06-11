---
name: jquiver-template-mail
description: Use for JQuiver template, email/XML, and webclient XML analysis or configuration involving jq_template_master, template consumers, merge fields, response producer types, mail rendering, and external XML payload safety.
---

# JQuiver Template Mail

## 1. Skill name
JQuiver Template Mail

## 2. Purpose
Guide an agent through safe review or modification of JQuiver templates, email/XML templates, webclient XML templates, and reusable fragments.

## 3. Trigger phrases / when to use
- "template"
- "templating"
- "email-xml"
- "webclient-xml"
- "email/XML"
- "template body"
- "merge fields"

## 4. Required context
- Template ID/name.
- Template category: page, fragment, email/XML, webclient XML, or unknown.
- Consumer route/API/mail flow.
- Merge variables.
- Test data and safe recipient/endpoint plan.

## 5. Files to read first
- `../../knowledge-base/08-templates.md`
- `../../knowledge-base/17-notifications-mail.md`
- `../../knowledge-base/07-dynamic-rest-api.md`
- `../../playbooks/configure-template-email-webclient.md`
- `../../reference/metadata-table-reference.md`

## 6. Step-by-step workflow
1. Inspect `jq_template_master`.
2. Identify all consumers: route, form, API, mail flow, or script library.
3. Verify response producer type if used by Dynamic REST.
4. Validate merge variables and escaping.
5. For email/XML, test only with approved test recipients.
6. For webclient XML, use a test endpoint or dry-run process if available.
7. Preserve previous body and checksum.
8. Test all known consumers after change.

## 7. Output format
Return:
- Template map.
- Consumer list.
- Template category.
- Merge variable findings.
- Side effects and risks.
- TODOs and rollback plan.

## 8. Safety rules
- Do not trigger production email or external calls during analysis.
- Redact recipients, URLs, headers, tokens, and private content.
- Preserve checksum/custom-update behavior unless verified.
- Recommend backup before template changes.

## 9. Examples
- A Dynamic REST API may render `email/xml` and then send mail.
- A public job page may be a route-backed template using grids and APIs.

## 10. Things not to do
- Do not edit shared templates without listing consumers.
- Do not break XML/HTML escaping.
- Do not expose secrets in webclient XML.
- Do not assume a template is only presentational.
