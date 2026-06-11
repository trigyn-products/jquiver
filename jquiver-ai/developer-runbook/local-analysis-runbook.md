# Local Analysis Runbook

## Purpose
Guide safe read-only analysis of a local JQuiver instance folder or export.

## Prerequisites
- User permission to read the target folder.
- Clear constraints on what not to read.
- SQL exports available.
- Upload folder analysis permitted if needed.

## Inputs required
- Instance folder path: `<INSTANCE_FOLDER>`.
- SQL files: `<SQL_FILE_LIST>`.
- Upload folder path: `<UPLOAD_FOLDER_PATH>`.
- Allowed runtime URL: `<LOCAL_URL>` if available.
- User constraints: `<CONSTRAINTS>`.

## Steps
1. Record user constraints.
2. Inventory files and folders.
3. Identify SQL exports and schema names.
4. Do not read forbidden files.
5. Do not explode JARs unless explicitly authorized.
6. Parse table names, views, row counts, and populated tables.
7. Separate JQuiver core/platform tables from custom module/business tables using the approved core-table rule.
8. Map routes, forms, grids, APIs, datasources, dashboards, file bins, schedulers, and autocompletes.
9. Inspect upload folder counts and file associations if allowed.
10. If a local URL is provided, check read-only reachability first.
11. Summarize structure and known facts without exposing secrets or PII.

## Validation checklist
- Constraints followed.
- SQL files identified.
- Schemas identified.
- Metadata modules mapped.
- Custom schemas mapped.
- Upload folder summarized safely.
- Sensitive values redacted.
- Unknowns marked TODO.

## Common errors
- Reading forbidden configuration files.
- Exploding JARs without authorization.
- Printing datasource credentials.
- Printing applicant/user PII.
- Treating local URL behavior as complete UI validation when browser automation failed.

## Rollback/safety notes
- This runbook is read-only.
- If any command could modify files or databases, stop and get explicit approval.
- Do not create dumps unless the user asks.

## Related KB/reference/playbook files
- `database-schema-analysis.md`
- `safe-data-handling.md`
- `../playbooks/analyze-existing-instance.md`
- `../knowledge-base/18-ark-pharma-instance.md`
- `../knowledge-base/19-sbi-fac-instance.md`
