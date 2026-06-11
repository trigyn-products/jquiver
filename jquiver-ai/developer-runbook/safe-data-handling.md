# Safe Data Handling

## Purpose
Define safe handling rules for JQuiver SQL dumps, metadata, logs, uploads, credentials, and PII.

## Prerequisites
- Understand whether data is local, development, staging, or production.
- Know whether the task requires reading sensitive data.
- Have user approval for any sensitive inspection.

## Inputs required
- Data source: `<DATA_SOURCE>`.
- Environment: `<ENVIRONMENT_NAME>`.
- Data categories present: credentials, users, applicants, uploads, logs, mail, or business data.
- Output destination: chat, file, report, or generated artifact.

## Steps
1. Classify the data before reading or summarizing it.
2. Identify secrets such as passwords, tokens, connection strings, OTPs, and private keys.
3. Identify PII such as names, emails, phone numbers, addresses, resumes, and user records.
4. Redact secrets and PII in summaries.
5. Prefer counts, schemas, relationships, and examples with safe values.
6. Avoid opening uploaded files unless authorized and necessary.
7. Avoid copying raw logs with private payloads.
8. Do not commit secrets or PII to the KB.
9. Use placeholders such as `<DB_PASSWORD>`, `<USER_EMAIL>`, and `<UPLOAD_PATH>`.
10. If sensitive exposure occurs, stop and notify the user.

## Validation checklist
- Data classified.
- Secrets redacted.
- PII redacted.
- Uploaded files not exposed.
- Logs sanitized.
- Output contains placeholders where needed.
- User constraints followed.

## Common errors
- Printing datasource passwords from `jq_additional_datasource`.
- Printing applicant emails or resume metadata unnecessarily.
- Copying file paths that reveal private infrastructure.
- Including raw mail content or OTP values.
- Treating uploaded files as harmless because filenames look generic.

## Rollback/safety notes
- If sensitive data is written to a file, remove or sanitize it immediately with user awareness.
- If sensitive data is shared outside approved channels, follow the organization's incident process.
- Do not create additional copies of sensitive dumps unless explicitly needed and approved.

## Related KB/reference/playbook files
- `../AGENTS.md`
- `../knowledge-base/30-ai-agent-working-rules.md`
- `database-backup-restore.md`
- `local-analysis-runbook.md`
- `../reference/ai-agent-review-checklist.md`

