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
- Keep JQuiver feature work metadata/configuration driven; do not create Spring Boot Controller, Service, Repository/DAO, DTO, request/response, or Entity classes unless explicitly asked or verified metadata patterns are insufficient.
- A user-facing screen is incomplete without its full metadata chain: query/API, grid or form, template if needed, router/module, menu if user-facing, role/entity access, and resource bundle labels when required.
- Redact credentials and PII.
- Do not call side-effecting APIs unless explicitly asked.
- Do not generate destructive SQL unless explicitly asked.
- Follow playbooks before making changes.
- Keep assumptions visible.
- When analyzing an instance DB, do not assume every table belongs to JQuiver core. Treat non-`jq_*` tables, except `flyway_schema_history`, `mail_schedule`, `persistent_logins`, and `qrtz_*`, as custom business/module tables.
- Use SQL exports only as private evidence. Summaries may include table names, safe column names, feature mapping, and instance name; do not include raw SQL dumps, INSERT statements, row data, credentials, or personal/candidate/employee/mail data.
- Before creating or modifying router/menu/grid/API navigation, inspect `application.yml` or `application.yaml` and at least one existing working module. Use configured prefixes from `application.yml`; defaults are `/view` for router pages and `/api` for REST APIs.
- When phase-wise implementation is requested, do only the requested phase and stop.
- Avoid broad exploration; do not repeatedly scan all `jq_*` tables or unrelated modules unless the exact task requires it.
- Do not query the database unless the user explicitly asks for DB verification or the task cannot be completed without checking a specific metadata ID.
- Do not run `mvn`, `mvnw`, package, compile, or test commands for documentation/metadata-only tasks.
- If DDL is already executed, treat business tables as existing baseline and do not recreate or alter DDL unless asked.
- Never generate generic Form Builder HTML. Inspect and adapt an existing verified JQuiver form pattern for buttons, validation, messages, field highlighting, save behavior, and navigation.
- Reject or revise Form Builder output that left-aligns buttons without verified precedent, uses custom generic Save/Cancel handlers, skips required-field highlighting, hardcodes `/cf/*`, attaches multiple save queries without verified reason, or omits `view.path`/`api.path` verification.

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
- `view.path`/`api.path` and existing working link patterns.
- Role/entity/module access metadata for every new screen/action.
- Existing verified Form Builder example used, when form HTML is generated.
- Form Builder buttons, validation, messages, field highlighting, save query, and navigation match the verified example.
- Backup and rollback plan.

## TODO items to verify
- TODO: verify organization-specific approval rules.
- TODO: verify production change window and rollback requirements.
- TODO: verify official agent workflow once source repository is connected.

## Example
If asked to "fix the scheduler," do not immediately update cron metadata. First inspect scheduler row, target API, logs, side effects, environment, and backup requirements.
