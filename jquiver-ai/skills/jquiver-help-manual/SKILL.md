---
name: jquiver-help-manual
description: Use for JQuiver help manual analysis, configuration, and troubleshooting involving help manual metadata, module associations, helpManual file bins, uploaded assets, multilingual help content, and visibility.
---

# JQuiver Help Manual

## 1. Skill name
JQuiver Help Manual

## 2. Purpose
Guide an agent through safe configuration or troubleshooting of JQuiver help manuals and help assets.

## 3. Trigger phrases / when to use
- "help manual"
- "manual entry"
- "help page"
- "helpManual file bin"
- "manual attachment"
- "module help"

## 4. Required context
- Target module/route.
- Help manual title/content.
- Language requirements.
- Attachments or screenshots.
- Audience/visibility rules.

## 5. Files to read first
- `../../knowledge-base/10-help-manual.md`
- `../../knowledge-base/09-file-upload.md`
- `../../playbooks/configure-help-manual.md`
- `../../reference/metadata-table-reference.md`
- `../../developer-runbook/safe-data-handling.md`

## 6. Step-by-step workflow
1. Identify target module/route.
2. Inspect existing help manual metadata in the target instance.
3. Verify exact help manual table names from database/source.
4. Inspect file bin behavior for manual attachments.
5. Verify multilingual needs and resource references.
6. Test visibility from the target page and roles.
7. Validate attachment rendering/download permissions.
8. Record TODOs for unknown source behavior.

## 7. Output format
Return:
- Module/help association.
- Help content and assets.
- File bin usage.
- Language/visibility notes.
- Risks/TODOs.
- Test checklist.

## 8. Safety rules
- Do not expose private screenshots or documents.
- Verify file permissions for help attachments.
- Recommend backup before metadata writes.
- Mark exact table names as TODO until verified.

## 9. Examples
- HRS includes a `helpManual` file bin for help assets.
- Help content may need route association and file upload metadata.

## 10. Things not to do
- Do not assume help manual table names without verification.
- Do not upload sensitive screenshots.
- Do not make help assets public unless approved.
- Do not ignore multilingual requirements.
