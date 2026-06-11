---
name: jquiver-instance-analyzer
description: Use for read-only JQuiver instance analysis involving SQL exports, schemas, metadata tables, routes, forms, grids, APIs, datasources, dashboards, schedulers, file bins, uploaded files, and sensitive-data redaction.
---

# JQuiver Instance Analyzer

## 1. Skill name
JQuiver Instance Analyzer

## 2. Purpose
Guide an agent through read-only analysis of a JQuiver instance export, database schema, upload folder, or running instance behavior.

## 3. Trigger phrases / when to use
- "analyze this instance"
- "read this SQL"
- "learn this JQuiver folder"
- "schema analysis"
- "metadata map"
- "do not generate anything"
- "instance summary"

## 4. Required context
- Instance folder or SQL path.
- Databases/schemas to analyze.
- User constraints, such as files not to read.
- Whether runtime URL/browser access is allowed.
- Sensitive-data handling expectations.

## 5. Files to read first
- `../../knowledge-base/00-overview.md`
- `../../knowledge-base/02-core-database-model.md`
- `../../knowledge-base/23-jquiver-metadata-tables-reference.md`
- `../../developer-runbook/local-analysis-runbook.md`
- `../../developer-runbook/database-schema-analysis.md`
- `../../developer-runbook/safe-data-handling.md`
- `../../playbooks/analyze-existing-instance.md`

## 6. Step-by-step workflow
1. Confirm read-only boundaries.
2. Respect explicit exclusions such as config files or JARs.
3. Identify SQL files, upload folders, and instance-specific assets.
4. Count tables, views, inserts, and major prefixes.
5. Map metadata families: routes, forms, grids, APIs, datasources, dashboards, dashlets, schedulers, file bins, autocompletes.
6. Identify custom module/business tables and custom schemas.
7. Redact PII, credentials, tokens, and private files.
8. Use streaming analysis for large SQL files.
9. Report only what the user requested.

## 7. Output format
Return:
- If the user asked only to read: a terse completion.
- If analysis is requested: schema/module map, metadata counts, key dependencies, redaction notes, and TODOs.

## 8. Safety rules
- Do not read excluded files.
- Do not explode JARs unless explicitly authorized.
- Do not create dumps unless explicitly requested.
- Do not print secrets or person-level data.

## 9. Examples
- ARK analysis included main/custom schemas and Form.io embed files.
- HRS analysis identified audit, interview, scheduler, dashboard, API client, and business module metadata.

## 10. Things not to do
- Do not generate documentation when the user said only read.
- Do not treat table names as full business rules.
- Do not run destructive SQL.
- Do not browse/runtime-test side-effecting endpoints without permission.
