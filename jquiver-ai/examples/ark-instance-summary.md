# Ark Pharma Instance Summary

## Status
Sample instance summary based on observed exported metadata. This is not a final factual document and must be verified against the current Ark export, database, source code, and running instance before use.

## Purpose
Show how an AI agent should summarize an analyzed JQuiver instance without exposing credentials or private data.

## Sample Summary

Instance family:
- Ark Pharma Returns-style JQuiver implementation.

Observed schema pattern:
- Main JQuiver/platform schema: `ark_pharma_knowledge_base`.
- Custom domain schema: `ark_pharma_knowledge_base_custom`.

Observed module areas:
- Policy details.
- Policy exception handling.
- Product/manufacturer lookup.
- Hospital details.
- Return instructions.
- Email template mapping.
- Custom lookup and manufacturer/processor/wholesaler support.

Observed JQuiver capabilities:
- Metadata-driven routes, forms, grids, Dynamic REST APIs, autocompletes, file bins, and templates.
- Additional datasource entry pointing to a custom schema.
- Form.io/pluggable form embed examples for single-submit and multi-submit flows.

JQuiver platform metadata/core tables used by the instance:
- `jq_*` metadata tables for routes, forms, grids, Dynamic REST, autocompletes, file bins, templates, and additional datasource configuration.
- Core support tables such as `flyway_schema_history`, `mail_schedule`, `persistent_logins`, and `qrtz_*` if present in the export.

Custom business/module tables created for this instance:
- Non-core tables in the ARK main/custom schemas, including policy, policy exception, product/manufacturer, hospital, return instruction, email-template mapping, and lookup/support tables.
- Treat every non-`jq_*` table as custom unless it is `flyway_schema_history`, `mail_schedule`, `persistent_logins`, or `qrtz_*`.

Observed external embedding pattern:
- `single submit.html` and `multi submit.html` demonstrate iframe-based embedding of JQuiver Form.io/pluggable routes.
- Treat URLs in historical examples as environment-specific and replace them with placeholders in docs.

## Safe Notes
- Do not expose datasource credentials from export files.
- Do not expose uploaded files or private documents.
- Verify route names, table counts, and current schemas from the latest export.
- Do not include raw row data, credentials, or uploaded document content.

## Suggested Agent Output Shape

```text
Instance: Ark Pharma Returns sample
Schemas: <main_schema>, <custom_schema>
Major modules: policy, manufacturer/product lookup, return instruction, hospital details
Key JQuiver features: forms, grids, APIs, autocomplete, file upload, additional datasource, Form.io embed
Open TODOs: verify current export, route permissions, datasource credentials, upload storage
```
