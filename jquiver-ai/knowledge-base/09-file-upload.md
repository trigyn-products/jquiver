# File Upload

## Purpose
Explain JQuiver file upload bins, file metadata, storage, and safe handling.

## When to use this file
Use this file when configuring uploads, downloads, file bins, upload validation, file storage, or uploaded-file analysis.

## Related files
- `13-additional-datasource.md`
- `15-security-users-roles.md`
- `../playbooks/configure-file-upload-bin.md`
- `../developer-runbook/troubleshoot-file-upload.md`
- `../developer-runbook/safe-data-handling.md`

## Known facts
- Observed file bin metadata uses `jq_file_upload_config`.
- Observed file metadata uses `jq_file_upload`.
- ARK has bins such as `arkemployee`, `arkpolicytest_fb`, and `ark_hospital_certificate`.
- SBI FAC has `applicantresume_fb` for applicant resumes.
- HRS has bins such as `default`, `helpManual`, `interviewFeedbackBin`, `intrv_candidate_pic`, `profilePic`, `QMS_Module`, `traineeConfirmationBin`, and `vms_files`.
- Analyzed upload folders contained opaque stored files and database metadata mapping original names to physical paths.

## File bin types
Default file bins:
- Use standard JQuiver storage behavior.
- Are usually configured by `file_bin_id`, allowed file patterns, maximum file size, number of files, and upload/view/delete query metadata.
- Should be used unless the target workflow has a clear custom storage or custom validation need.

Custom file bins:
- May use custom upload, view, delete, or storage behavior.
- Observed HRS metadata includes file bin fields such as `upload_query_content`, `view_query_content`, `delete_query_content`, `datasource_id`, `is_file_storage_enable`, and `custom_file_storage_class`.
- Treat custom storage class names as source-code dependencies. Do not assume they exist in another instance.
- Verify custom class behavior from source before migration or reuse.

Common custom-bin examples:
- Resume or candidate document bins.
- Help manual attachment bins.
- Interview feedback attachment bins.
- Profile picture bins.
- Audit/QMS evidence bins.

## File lifecycle
1. A form or API exposes upload UI/behavior.
2. File bin metadata defines allowed file types, max size, file count, and storage rules.
3. File is stored in the configured file location or custom storage implementation.
4. Metadata row records file identity, original name, physical name/path, updater, timestamp, and association ID.
5. Download/view APIs or UI links retrieve the file.

## Relationships
- File bin -> upload constraints.
- Form/API -> file bin ID.
- File metadata -> file association ID.
- File metadata -> physical storage path.
- File access -> route/API/security configuration.

## Safe AI-agent usage
- Treat uploads as sensitive.
- Do not print resume contents, private PDFs, images, or personal documents.
- Verify allowed file extensions and size limits.
- Check file serving permissions for public routes.
- For custom file bins, verify custom class/source-code behavior before copying metadata.
- Never delete physical upload files unless explicitly asked and after a backup.
- Recommend backup before moving or deleting upload folders.

## TODO items to verify
- TODO: verify physical storage, encryption, and file serving behavior from source code.
- TODO: verify file cleanup scheduler behavior.
- TODO: verify allowed file type matching rules.
- TODO: verify custom storage class behavior.
- TODO: verify exact upload/view/delete query execution contract for custom file bins.

## Example
For a resume upload issue in SBI FAC, inspect `applicantresume_fb`, file metadata, physical upload folder, applicant association, and download API security.
