# Troubleshoot File Upload

## Purpose
Troubleshoot JQuiver file upload, download, file bin, and storage issues.

## Prerequisites
- Target file bin or form/API identified.
- Permission to inspect file metadata.
- Permission to inspect upload folder if needed.
- Sensitive-file handling rules understood.

## Inputs required
- File bin ID: `<FILE_BIN_ID>`.
- Related form/API/route: `<RELATED_MODULE>`.
- File association ID, if known.
- Upload folder path: `<UPLOAD_FOLDER_PATH>`.
- Symptom: upload fail, download fail, file missing, wrong type, too large, permission issue, or cleanup issue.

## Steps
1. Identify the related form, API, or route.
2. Inspect `jq_file_upload_config` for the file bin.
3. Verify allowed extensions, max file size, and file count.
4. Verify datasource fields if custom validation/storage queries are configured.
5. Inspect `jq_file_upload` metadata for affected files.
6. Verify physical file path or storage location.
7. Check file association ID.
8. Check route/API permissions for download/view.
9. Check cleanup scheduler if files disappear.
10. Check logs for size/type/path errors.
11. Do not open private uploaded files unless necessary and authorized.

## Validation checklist
- File bin exists.
- Allowed type matches uploaded file.
- Max size not exceeded.
- File count not exceeded.
- Metadata row exists.
- Physical file exists.
- Download route/API permitted.
- Sensitive file contents not exposed.

## Common errors
- Wrong file bin ID.
- File extension not allowed.
- Upload folder missing or not writable.
- Metadata exists but physical file missing.
- Physical file exists but metadata missing.
- User lacks download permission.
- Cleanup job removed temp files.

## Rollback/safety notes
- Back up file metadata before edits.
- Back up upload folder before moving/deleting files.
- Do not delete physical files without explicit approval.
- If restoring files, restore both metadata and physical storage consistently.

## Related KB/reference/playbook files
- `../knowledge-base/09-file-upload.md`
- `../playbooks/configure-file-upload-bin.md`
- `safe-data-handling.md`
- `troubleshoot-scheduler.md`
- `database-backup-restore.md`

