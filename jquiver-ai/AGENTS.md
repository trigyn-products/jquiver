# JQuiver AI Agent Instructions

## Purpose
Define how AI agents must use this repository when analyzing, documenting, or proposing changes for the JQuiver low-code platform.

This file is the operating contract for agents. It favors verified JQuiver evidence over plausible guesses.

## When to use this file
Use this file before an AI agent analyzes, edits, generates, or reviews JQuiver-related documentation, SQL, metadata, source code, source exports, instance folders, service-layer behavior, or configuration.

## Related files
- `KB-MAP.md`
- `README.md`
- `reference/ai-agent-review-checklist.md`
- `reference/metadata-table-reference.md`
- `knowledge-base/30-ai-agent-working-rules.md`
- `developer-runbook/safe-data-handling.md`

## Known facts
- JQuiver behavior can be changed through database metadata.
- Database exports may contain credentials, PII, user information, applicant information, uploaded-document metadata, and operational configuration.
- The analyzed instance exports show metadata-driven modules for routes, forms, Form.io/pluggable forms, grids, dynamic REST APIs, templates, file upload, help manual, dashboards, dashlets, multilingual resources, additional datasource, scheduler, users, roles, and permissions.
- Verified table names include examples such as `jq_module_listing`, `jq_dynamic_form`, `jq_grid_details`, `jq_dynamic_rest_details`, `jq_dynamic_rest_dao_details`, `jq_autocomplete_details`, `jq_additional_datasource`, `jq_file_upload_config`, `jq_dashboard`, `jq_dashlet`, `jq_resource_bundle`, `jq_user`, and `jq_role`.
- Exact table names are considered verified only when present in one of the supplied SQL exports. Runtime URL behavior, source-code behavior, and business rules must still be verified from the running application or source code.
- JQuiver core/platform tables are identified as `jq_*`, `flyway_schema_history`, `mail_schedule`, `persistent_logins`, and `qrtz_*`. All other tables in instance SQL exports must be treated as custom module/business tables unless explicitly documented otherwise.
- `jq_security_type` and `jq_security_properties` are verified in the supplied `hrsdev.sql` export.
- This repository is v1 documentation and must continue to be enriched from verified JQuiver source code, database schemas, running instances, and exported metadata.

## TODO items to verify
- TODO: Verify organization-specific approval rules for production metadata changes.
- TODO: Verify official JQuiver source-code paths, package names, controller/service classes, and deployment process.
- TODO: Verify exact metadata table semantics by JQuiver version.
- TODO: Verify security and permission flow from current source code.

## Core Rules
- Do not invent JQuiver internals.
- Prefer actual source code, database schema, exported metadata, and running instance behavior.
- JQuiver feature development must be metadata/configuration driven. Do not implement normal Spring Boot CRUD using Controller, Service, Repository, DAO, DTO, request/response, or Entity classes unless the user explicitly asks for custom Java code or a verified JQuiver pattern proves metadata is insufficient.
- Mark assumptions clearly.
- Mark unknown exact details as `TODO: Verify from actual JQuiver source code / database / instance export.`
- Never generate destructive SQL unless explicitly asked.
- Always recommend backup before metadata or database changes.
- Follow playbooks before making changes.
- A user-facing JQuiver screen is not complete until the full metadata chain is handled: query/API metadata, grid or form metadata, template if needed, router/module entry, menu linkage if user-facing, role/entity access, and resource bundle/i18n labels when required.
- Every new JQuiver screen/action must include role/entity/module access metadata according to the existing JQuiver RBAC pattern; do not leave screens inaccessible due to missing role mappings.
- Use reference files for exact table names, naming conventions, security rules, and environment configuration.
- For new AI tools, do not create duplicated full instructions. Generate a thin adapter file that points to `KB-MAP.md`, `AGENTS.md`, the relevant playbook, and the relevant skill.
- Before creating JQuiver router/menu/grid/API links, read `application.yml` or `application.yaml`. Router prefix defaults to `/view`, API prefix defaults to `/api`, but both are configurable. Use `{view.path}/{router-path}` and `{api.path}/{api-path}`. Never invent `/cf/*` unless configured or verified.
- Never generate generic Form Builder HTML. For JQuiver forms, inspect and reuse an existing verified form pattern for buttons, validation, messages, field highlighting, save behavior, and navigation. Read `application.yml`/`application.yaml` for `view.path` and `api.path`. Do not hardcode `/cf/*` unless configured or verified.
- When the user asks for phase-wise implementation, do only the requested phase and stop. Do not continue to dashboards, reports, security, documentation, tests, or extra screens unless explicitly in the current phase.
- Avoid broad exploration. Inspect only the files, metadata, or docs needed for the exact task; do not repeatedly scan all `jq_*` tables or unrelated modules.
- Do not query the database unless the user explicitly asks for DB verification or the task cannot be completed without checking a specific metadata ID.
- Do not run `mvn`, `mvnw`, package, compile, or test commands for documentation/metadata-only tasks.
- If the user says DDL is already executed, treat business tables as an existing baseline. Do not recreate, regenerate, or alter DDL unless explicitly asked.
- Do not expose credentials, datasource passwords, applicant PII, resume content, private user data, tokens, OTPs, or secrets from dumps.
- Do not read or rely on unconfigured environment files unless the user explicitly authorizes it.
- Do not call side-effecting dynamic REST APIs, scheduler endpoints, delete APIs, save APIs, or email APIs during analysis unless explicitly asked.

## Evidence Priority
Use this order when resolving uncertainty:

1. Current JQuiver source code.
2. Target instance database schema and exported metadata.
3. Running instance behavior observed through browser or service-layer calls.
4. Existing working metadata rows in the same instance.
5. This knowledge base.
6. Clearly labeled assumptions.

## Working Method
1. Identify the target JQuiver module or metadata area.
2. Open `KB-MAP.md` and follow the task-specific reading list.
3. Read the relevant `knowledge-base/`, `reference/`, `developer-runbook/`, `playbooks/`, and `skills/` files.
4. Verify exact table names, route names, API names, code paths, query types, security flags, and configuration keys from the target source/export.
5. Recommend a backup before proposing SQL or metadata changes.
6. Prefer read-only diagnostics first.
7. State unresolved TODOs instead of filling gaps with guesses.
8. Keep sensitive values redacted in summaries and generated artifacts.
9. Use SQL exports only as private evidence. Do not copy raw SQL dumps, INSERT statements, row data, credentials, or personal/candidate/employee/mail data into KB files.

## SQL Safety
- Safe by default: `SELECT`, schema inspection, counts, view inspection, dependency mapping.
- Requires explicit user request and backup recommendation: `INSERT`, `UPDATE`, `DELETE`, `ALTER`, `DROP`, `TRUNCATE`, stored procedure execution, scheduler execution.
- Destructive SQL must include purpose, target tables, expected impact, rollback strategy, and test plan.

## Metadata Change Safety
Before changing JQuiver metadata, verify:
- Which module owns the metadata.
- Whether the metadata is system-owned or custom.
- Which route/page/API/grid/form/dashlet/file bin uses it.
- Which roles or entity permissions apply.
- Which datasource and schema the query targets.
- Whether the change affects public or anonymous users.
- Whether the change affects production data.

## Data Handling Rules
- Redact datasource credentials even if present in SQL dumps.
- Summarize applicant, user, resume, hospital contact, and email data without printing private values.
- Avoid copying uploaded file contents into documentation.
- Treat file-upload folders as potentially sensitive.
- Keep examples generic unless the values are already non-sensitive and useful for understanding.

## Expected Agent Output
When answering JQuiver questions, include:
- Verified facts.
- Source of verification, such as schema/export/running instance/source code.
- TODOs for unverified details.
- Safety notes for metadata, SQL, or production behavior.
- Relevant next files/playbooks to read when useful.

## Example
Before changing a grid:

1. Read `KB-MAP.md`.
2. Read `knowledge-base/06-grid-utils.md`.
3. Read `reference/metadata-table-reference.md`.
4. Follow `playbooks/create-grid.md` or `playbooks/modify-existing-module.md`.
5. Verify `jq_grid_details` in the target database.
6. Verify the target table/view and `datasource_id`.
7. Recommend backup before any metadata update.
8. Mark unknown runtime behavior as TODO.
