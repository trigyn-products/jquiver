---
name: jquiver-file-upload
description: Use for JQuiver file upload and file bin analysis, configuration, or troubleshooting involving jq_file_upload_config, jq_file_upload, default/custom file bins, upload/view/delete behavior, storage locations, and file privacy.
---

# JQuiver File Upload

## 1. Skill name
JQuiver File Upload

## 2. Purpose
Guide an agent through safe work on JQuiver file bins, file metadata, custom storage behavior, and upload/download troubleshooting.

## 3. Trigger phrases / when to use
- "file bin"
- "custom file bin"
- "upload not working"
- "download file"
- "resume upload"
- "profile picture"
- "jq_file_upload_config"

## 4. Required context
- File bin ID.
- Related form/API/template.
- Allowed file types.
- Max file size and file count.
- Storage mode: default or custom.
- Download/view permissions.

## 5. Files to read first
- `../../knowledge-base/09-file-upload.md`
- `../../playbooks/configure-file-upload-bin.md`
- `../../developer-runbook/troubleshoot-file-upload.md`
- `../../developer-runbook/safe-data-handling.md`
- `../../reference/security-permission-matrix.md`

## 6. Step-by-step workflow
1. Inspect `jq_file_upload_config`.
2. Inspect `jq_file_upload` metadata for existing files.
3. Identify default vs custom bin behavior.
4. Verify custom class and upload/view/delete queries if present.
5. Confirm physical storage location only if authorized.
6. Check related form/API/template usage.
7. Test allowed type, blocked type, size limit, count limit, and download access.
8. Review cleanup scheduler impact.
9. Preserve files unless deletion is explicitly approved.

## 7. Output format
Return:
- File bin ID.
- Constraints and storage behavior.
- Consumer map.
- File metadata/physical storage findings.
- Privacy risks.
- Validation and rollback steps.

## 8. Safety rules
- Treat uploaded files as sensitive.
- Do not print resume/PDF/image contents.
- Do not delete physical files without explicit approval and backup.
- Verify public download permissions carefully.

## 9. Examples
- SBI FAC uses applicant resume upload bins.
- HRS has bins for help manuals, interview feedback, candidate pictures, and QMS evidence.

## 10. Things not to do
- Do not broaden allowed file types casually.
- Do not copy custom storage class names without source verification.
- Do not assume metadata exists just because the physical file exists.
- Do not expose private files through public routes.
