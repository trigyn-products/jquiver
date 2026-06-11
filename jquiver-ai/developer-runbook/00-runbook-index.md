# Developer Runbook Index

## Purpose
Provide the entry point for operational JQuiver runbooks.

Use this index to select the correct runbook before setting up, running, analyzing, troubleshooting, backing up, restoring, deploying, or safely handling JQuiver data.

## Prerequisites
- Read `../AGENTS.md`.
- Read `../KB-MAP.md`.
- Confirm the target instance, environment, and task.
- Confirm whether the work is read-only or may change metadata/data.

## Inputs required
- Target task.
- Target environment, such as local, dev, staging, or production.
- Target instance name.
- Available source code, SQL export, database access, logs, or URL.
- User constraints, such as files not to read or APIs not to call.

## Steps
1. Identify the task category.
2. If the task is setup or runtime, use `setup-local-development.md`, `build-and-run-jquiver.md`, or `environment-configuration.md`.
3. If the task involves SQL, use `database-schema-analysis.md`, `common-sql-diagnostics.md`, or `database-backup-restore.md`.
4. If the task involves metadata navigation, use `jquiver-metadata-navigation.md`.
5. If the task is troubleshooting, choose the module-specific troubleshoot runbook.
6. If the task involves deployment, use `release-and-deployment-runbook.md`.
7. If sensitive data may appear, use `safe-data-handling.md` first.
8. Read the matching playbook before proposing changes.

## Validation checklist
- Correct runbook selected.
- Relevant KB and reference files identified.
- Target environment confirmed.
- Read-only vs write operation confirmed.
- Backup requirement assessed.
- Sensitive-data handling rules applied.

## Common errors
- Starting with SQL changes before reading the correct playbook.
- Treating local findings as production facts without verification.
- Ignoring custom datasource dependencies.
- Forgetting file-upload folder dependencies during migration.

## Rollback/safety notes
- This index does not authorize changes.
- Follow the specific runbook and playbook for backup, rollback, and validation.
- For production issues, prefer read-only diagnostics until an approved change plan exists.

## Related KB/reference/playbook files
- `../AGENTS.md`
- `../KB-MAP.md`
- `../reference/ai-agent-review-checklist.md`
- `../knowledge-base/30-ai-agent-working-rules.md`
- `../playbooks/debug-production-issue.md`

