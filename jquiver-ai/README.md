# JQuiver AI Knowledge Base

## Purpose
This repository is an AI Knowledge Base and Agent Enablement Pack for the JQuiver low-code platform.

It is designed to help AI agents and developers understand, analyze, document, troubleshoot, and safely propose changes to JQuiver instances. The pack is intentionally evidence-first: it should be enriched from actual JQuiver source code, database schemas, exported metadata, running instance behavior, and real instance configurations.

## When to use this file
Use this file when you need to understand what this repository is, who it is for, how an AI agent should consume it, and what each folder means.

## Related files
- [AGENTS.md](AGENTS.md)
- [KB-MAP.md](KB-MAP.md)
- [CONTRIBUTING.md](CONTRIBUTING.md)
- [reference/metadata-table-reference.md](reference/metadata-table-reference.md)
- [knowledge-base/30-ai-agent-working-rules.md](knowledge-base/30-ai-agent-working-rules.md)

## Who should use this KB
- AI agents working on JQuiver analysis, documentation, metadata review, SQL review, or troubleshooting.
- Developers onboarding to JQuiver metadata-driven modules.
- Support engineers diagnosing instance issues.
- Architects comparing JQuiver instances or planning migrations.
- Teams creating or reviewing Form Builder forms, Form.io forms, grids, Dynamic REST APIs, dashboards, schedulers, file upload bins, multilingual labels, script libraries, API clients, business modules, tags, notifications, templates, or additional datasource integrations.

## How AI agents should consume this KB
1. Start with [AGENTS.md](AGENTS.md) for rules and safety constraints.
2. Open [KB-MAP.md](KB-MAP.md) and choose the task-specific reading list.
3. Read the relevant conceptual page in `knowledge-base/`.
4. Read exact table and naming references in `reference/`.
5. Follow the relevant workflow in `playbooks/`.
6. Use `developer-runbook/` for diagnostics, environment, backup, and troubleshooting steps.
7. Use `skills/` when acting as a focused JQuiver specialist.
8. Treat `fine-tuning-data/` as seed examples, not as a complete training corpus.
9. Treat `examples/` as practical patterns, not as universal implementation truth.

Agents must not invent unknown implementation details. If exact table semantics, source-code paths, APIs, query types, configuration keys, or security behavior are not verified, write:

`TODO: Verify from actual JQuiver source code / database / instance export.`

Exact table names are considered verified only when present in one of the supplied SQL exports. Runtime URL behavior, source-code behavior, and business rules must still be verified from the running application or source code.

JQuiver core/platform tables are identified as `jq_*`, `flyway_schema_history`, `mail_schedule`, `persistent_logins`, and `qrtz_*`. All other tables in instance SQL exports must be treated as custom module/business tables unless explicitly documented otherwise.

Schema classification note:

In JQuiver instance databases, platform/core tables are identified as:
- `jq_*`
- `flyway_schema_history`
- `mail_schedule`
- `persistent_logins`
- `qrtz_*`

All other tables should be treated as custom module/business tables belonging to the specific application instance.

## Folder Guide
- `knowledge-base/`: Conceptual JQuiver documentation by module or topic.
- `reference/`: Exact reference material such as metadata tables, naming conventions, UI standards, security matrix, environment references, glossary, and agent checklist.
- `developer-runbook/`: Operational procedures for setup, build/run, environment configuration, SQL diagnostics, backup/restore, deployment, and troubleshooting.
- `playbooks/`: Step-by-step workflows for common changes such as CRUD modules, forms, grids, APIs, datasources, schedulers, dashboards, labels, templates, script libraries, API clients, notifications, business modules, tags, and production debugging.
- `skills/`: Repository-local skill packs that define how an AI agent should behave for specific JQuiver tasks.
- `fine-tuning-data/`: Small JSONL seed examples for future fine-tuning or evaluation datasets.
- `examples/`: Concrete examples from analyzed JQuiver instances and reusable snippets such as Form.io iframe embedding.

## Known facts
- JQuiver uses database metadata to configure modules such as routes, forms, grids, dynamic REST APIs, file upload bins, dashboards, dashlets, templates, schedulers, resource bundles, autocompletes, and additional datasources.
- The analyzed examples include ARK Pharma and SBI FAC instance exports.
- ARK includes a main schema named `ark_pharma_knowledge_base` and a custom schema named `ark_pharma_knowledge_base_custom`.
- SBI FAC includes a main JQuiver/platform schema and a custom careers schema.
- `jq_additional_datasource` allows metadata to connect to custom or external databases.
- Form.io/pluggable forms can be embedded in external applications through iframe routes, based on the analyzed ARK examples.
- The analyzed HRS export adds coverage for script libraries, API clients, business modules, tags, notifications, Form.io metadata, richer scheduler/mail patterns, and activity-log/CosmosDB-style Dynamic REST integrations.
- The HRS export is verified from `hrsdev.sql` at schema/table-name level and includes `jq_security_type` and `jq_security_properties`.
- Dynamic REST platform options observed in HRS metadata include Java, FTL, JavaScript content, Python, and PHP. Exact runtime behavior must be verified from source.
- This repository is v1 and should be treated as a structured starting point, not a complete product manual.

## TODO items to verify
- TODO: Verify current JQuiver source-code module names and package paths.
- TODO: Verify supported JQuiver versions and compatibility notes.
- TODO: Verify official build, deployment, and environment practices.
- TODO: Verify exact security, permission, and authentication flow from source code.
- TODO: Verify all metadata table semantics from source migrations.

## Example
If an agent is asked to create a Dynamic REST API:

1. Read [AGENTS.md](AGENTS.md).
2. Open [KB-MAP.md](KB-MAP.md).
3. Follow the "Create Dynamic REST API" reading list.
4. Verify `jq_dynamic_rest_details` and `jq_dynamic_rest_dao_details` in the target database.
5. Recommend backup before writing metadata SQL.
6. Mark unknown request/response behavior as TODO.
