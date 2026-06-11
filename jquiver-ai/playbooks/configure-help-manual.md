# Configure Help Manual

## Goal
Add or modify JQuiver help manual content for a module or page.

## When to use
Use this playbook when users need contextual help, module guidance, screenshots, PDFs, or documentation assets inside JQuiver.

## Required inputs
- Target module/page.
- Help title.
- Help content.
- Asset files, if any.
- Target roles/audience.
- Language requirements.

## Files/tables/configuration to inspect first
- Help manual metadata tables. TODO: verify from database/source.
- `jq_file_upload_config` for `helpManual` bin.
- `jq_file_upload` for existing assets.
- Route/module metadata.
- Resource bundle rows if multilingual.

## Step-by-step implementation approach
1. Verify exact help manual metadata model in the target instance.
2. Identify target module/page.
3. Draft clear help content.
4. Sanitize screenshots and examples.
5. Upload or link assets using verified file-bin behavior.
6. Add help metadata after backup.
7. Add resource bundle labels if needed.
8. Test visibility from the target module.

## Validation checklist
- Help entry appears in correct module.
- Content renders.
- Assets load.
- No sensitive data in screenshots.
- Permissions correct.
- Multilingual labels work if configured.

## Common mistakes
- Inventing help table names.
- Uploading sensitive screenshots.
- Broken asset references.
- Help entry linked to wrong module.
- Missing resource labels.

## Rollback plan
- Restore previous help metadata.
- Remove or hide new help entry.
- Keep uploaded assets unless approved for deletion.
- Restore previous resource labels if changed.

## Related skills and reference docs
- `../skills/jquiver-help-manual/SKILL.md`
- `../knowledge-base/10-help-manual.md`
- `../knowledge-base/09-file-upload.md`
- `../developer-runbook/troubleshoot-file-upload.md`

