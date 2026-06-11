# JQuiver Architecture

## Purpose
Explain the high-level architecture of JQuiver without inventing source-code internals.

## When to use this file
Use this file when reasoning about how the runtime application, metadata tables, custom schemas, frontend pages, and integrations fit together.

## Related files
- `00-overview.md`
- `02-core-database-model.md`
- `13-additional-datasource.md`
- `24-jquiver-page-lifecycle.md`
- `28-integration-patterns.md`

## Known facts
- JQuiver is metadata-driven.
- Known technology context includes Java/Spring Boot, MariaDB, jQuery, pqGrid, Form.io integration, Dynamic REST APIs, and metadata-driven UI generation.
- JQuiver core/platform tables are `jq_*`, `flyway_schema_history`, `mail_schedule`, `persistent_logins`, and `qrtz_*`.
- Instance-specific custom module/business tables are all other tables unless explicitly documented otherwise; they can live in the main application schema or in additional custom schemas.
- `jq_additional_datasource` allows metadata to reference additional databases.

## Architecture layers
Runtime layer:
- Serves pages, APIs, files, scheduler jobs, authentication/session behavior, and metadata execution.
- Technology context suggests Java/Spring Boot, but exact packages/classes must be verified from source.

Metadata layer:
- Describes routes, forms, grids, APIs, dashboards, dashlets, resource bundles, schedulers, file bins, roles, users, and datasources.
- Changes to metadata can change runtime behavior.

Business-data layer:
- Holds custom domain tables and views.
- ARK uses pharma returns concepts such as policies, exceptions, products, manufacturers, return instructions, hospitals, and email templates.
- SBI FAC uses careers/applicant/interview concepts such as jobs, applicants, OTP, interview schedules, panels, feedback, and questions.

Frontend layer:
- Known context includes jQuery, pqGrid, templates, and Form.io/pluggable forms.
- Exact asset paths, component conventions, and rendering internals must be verified from source.

## Relationships
- A route can point to a dashboard, form, template, API, external URL, or other target type.
- A form or grid can query the default schema or an additional datasource.
- A scheduler can invoke a Dynamic REST API.
- A dashboard uses dashlets, and dashlets often execute SQL or render template-like content.
- A file upload bin defines upload constraints; file metadata tracks uploaded files.

## Safe AI-agent usage
When explaining architecture, distinguish verified facts from inferred relationships. Avoid claiming exact class names, package names, or controller methods until the actual source code is read.

## TODO items to verify
- TODO: verify Java package/module boundaries from actual JQuiver source code.
- TODO: verify controller/service request lifecycle from source code.
- TODO: verify frontend asset loading and template engine behavior.
- TODO: verify supported deployment topologies.

## Example
A custom grid in ARK can render data from `ark_pharma_knowledge_base_custom` because a grid metadata row references a datasource ID configured in `jq_additional_datasource`.
