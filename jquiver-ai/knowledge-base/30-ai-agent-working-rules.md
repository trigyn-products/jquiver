# AI Agent Working Rules

## Purpose
Define safe working rules for AI agents analyzing or modifying JQuiver.

## When to use this file
Use this file before an AI agent proposes SQL, metadata changes, code changes, troubleshooting steps, generated docs, or migration guidance.

## Related files
- `../AGENTS.md`
- `../KB-MAP.md`
- `../reference/ai-agent-review-checklist.md`
- `../developer-runbook/safe-data-handling.md`

## Known facts
- JQuiver metadata can affect runtime behavior immediately.
- SQL dumps may contain credentials, PII, and operational configuration.
- Dynamic REST APIs and schedulers may have side effects.

## Agent rules
- Start with `AGENTS.md` and `KB-MAP.md`.
- Prefer source code, schema, exported metadata, and runtime evidence.
- Mark unknowns as TODO.
- Recommend backup before metadata/database writes.
- Use read-only diagnostics first.
- Redact credentials and PII.
- Do not call side-effecting APIs unless explicitly asked.
- Do not generate destructive SQL unless explicitly asked.
- Follow playbooks before making changes.
- Keep assumptions visible.
- When analyzing an instance DB, do not assume every table belongs to JQuiver core. Treat non-`jq_*` tables, except `flyway_schema_history`, `mail_schedule`, `persistent_logins`, and `qrtz_*`, as custom business/module tables.
- Use SQL exports only as private evidence. Summaries may include table names, safe column names, feature mapping, and instance name; do not include raw SQL dumps, INSERT statements, row data, credentials, or personal/candidate/employee/mail data.

## Review checklist
Before producing a JQuiver change plan, verify:
- Target instance.
- Target module.
- Metadata tables involved.
- Business schema tables/views involved.
- Datasource IDs.
- Security/role behavior.
- File upload impact.
- Scheduler/API side effects.
- Backup and rollback plan.

## TODO items to verify
- TODO: verify organization-specific approval rules.
- TODO: verify production change window and rollback requirements.
- TODO: verify official agent workflow once source repository is connected.

## Example
If asked to "fix the scheduler," do not immediately update cron metadata. First inspect scheduler row, target API, logs, side effects, environment, and backup requirements.
