# Contributing

## Purpose
Define how contributors should enrich and maintain this JQuiver AI Knowledge Base.

The main rule is simple: add verified JQuiver knowledge, not confident guesses.

## When to use this file
Use this file before adding new facts, examples, playbooks, reference material, runbooks, fine-tuning examples, or skill instructions.

## Related files
- [AGENTS.md](AGENTS.md)
- [KB-MAP.md](KB-MAP.md)
- [reference/ai-agent-review-checklist.md](reference/ai-agent-review-checklist.md)
- [developer-runbook/safe-data-handling.md](developer-runbook/safe-data-handling.md)

## Contribution Principles
- Prefer source code, database schema, exported metadata, and running instance behavior.
- Keep assumptions explicit.
- Use TODO markers for unverified details.
- Redact credentials, secrets, PII, OTPs, private user data, and uploaded-document contents.
- Keep playbooks actionable and safe.
- Keep reference files precise.
- Keep knowledge-base files understandable to both developers and AI agents.
- Do not add destructive SQL examples unless clearly marked as requiring explicit user approval and backup.

## Required Markdown Shape
Every markdown file should include:

1. Title
2. Purpose
3. When to use this file
4. Related files
5. Known facts
6. TODO items to verify
7. Example section, where applicable

## How to Add Verified Facts
When adding a fact, include enough context for a future agent to know where it came from:

- Source code path and class/function name, if known.
- SQL export and table name, if learned from database.
- Running instance URL or service-layer endpoint, if learned from runtime behavior.
- Instance name, such as ARK or SBI FAC, if instance-specific.

Do not paste secrets or PII as evidence.

## How to Add TODOs
Use clear TODO markers:

`TODO: Verify from actual JQuiver source code / database / instance export.`

Use TODOs for:
- Exact query type IDs.
- API request/response payloads.
- Security and permission behavior.
- Environment keys.
- Source-code package paths.
- Scheduler side effects.
- File storage/encryption behavior.

## How to Update Playbooks
Playbooks should be procedural. Include:
- Preconditions.
- Files to read.
- Read-only diagnostics.
- Backup recommendation.
- Change steps.
- Validation steps.
- Rollback notes.
- TODOs for unverified implementation behavior.

## How to Update Skills
Skill files should be concise and action-focused. Include:
- Frontmatter with `name` and `description`.
- Purpose.
- When to use this file.
- Related files.
- Known facts.
- TODO items.
- Inputs required.
- Step-by-step instructions.
- Do/don't rules.
- Expected output.
- Validation checklist.
- Common errors.

## Known facts
- This repository intentionally separates verified facts from TODOs.
- Sensitive details from database dumps must be redacted.
- JQuiver instance behavior can be changed by metadata updates, so review and backup guidance matter.
- The current v1 content was bootstrapped from ARK Pharma and SBI FAC schema and instance observations.

## TODO items to verify
- TODO: Add project-specific review workflow after repository ownership is defined.
- TODO: Add preferred branch, commit, and pull request process.
- TODO: Add documentation versioning policy.
- TODO: Add source citation format once the JQuiver source repository is connected.

## Example
When adding a metadata-table fact:

Good:
`Observed grid metadata uses jq_grid_details in the ARK and SBI FAC exports. TODO: Verify full column semantics from source migrations.`

Avoid:
`All JQuiver grids always behave this way.`

