# Configure File Upload Bin

## Goal
Configure a JQuiver file upload bin safely.

## When to use
Use this playbook when a form/API needs uploads, downloads, file validation, or file association behavior.

## Required inputs
- File bin ID.
- Allowed extensions.
- Max file size.
- Max number of files.
- Related form/API.
- Storage behavior: default or custom.
- Custom storage class or custom upload/view/delete queries, if any.
- Download/view permission requirements.

## Files/tables/configuration to inspect first
- `jq_file_upload_config`.
- `jq_file_upload`.
- Related form/API/template.
- File upload location configuration.
- Security/permission metadata.
- Cleanup scheduler if relevant.

## Step-by-step implementation approach
1. Find similar file bin in same instance.
2. Decide whether the bin should use default storage or custom behavior.
3. Define allowed file types and max size.
4. For custom bins, verify custom storage class and upload/view/delete query behavior from source or working examples.
5. Configure upload bin after backup.
6. Connect form/API to file bin.
7. Test allowed file.
8. Test disallowed file.
9. Test max size and max count.
10. Test download/view permissions.
11. Verify metadata and physical file are both created.

## Validation checklist
- File bin exists.
- Allowed type works.
- Disallowed type rejected.
- Max size enforced.
- File count enforced.
- Metadata row created.
- Physical file stored.
- Download secured.
- Custom storage/query behavior verified if used.

## Common mistakes
- Using overly broad file types.
- Missing upload folder permission.
- File metadata created but physical file missing.
- Public download route exposes private files.
- Cleanup scheduler removes files unexpectedly.
- Copying a custom storage class name into an instance where the class is not deployed.

## Rollback plan
- Restore previous file bin metadata.
- Disconnect form/API from new file bin if needed.
- Preserve uploaded files unless approved for deletion.
- Restore upload folder backup if storage was changed.

## Related skills and reference docs
- `../skills/jquiver-file-upload/SKILL.md`
- `../knowledge-base/09-file-upload.md`
- `../developer-runbook/troubleshoot-file-upload.md`
- `../developer-runbook/safe-data-handling.md`
