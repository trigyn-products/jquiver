# JQuiver Overview

## Purpose
Introduce JQuiver as a metadata-driven low-code platform and explain how agents should think about JQuiver instances.

JQuiver applications are assembled from platform metadata plus optional custom business schemas. A running page is rarely "just code"; it may be the result of a route row, a template, a form definition, a grid definition, a Dynamic REST API, a file bin, role permissions, resource bundle labels, and datasource configuration working together.

## When to use this file
Use this file before analyzing any JQuiver instance, designing a module, troubleshooting a page, or generating JQuiver documentation.

## Related files
- `01-jquiver-architecture.md`
- `02-core-database-model.md`
- `23-jquiver-metadata-tables-reference.md`
- `24-jquiver-page-lifecycle.md`
- `30-ai-agent-working-rules.md`

## Known facts
- JQuiver is a low-code platform.
- JQuiver uses metadata-driven modules, routes, templates, forms, grids, Dynamic REST APIs, dashboards, dashlets, file upload bins, resource bundles, schedulers, additional datasource configuration, and security users/roles.
- Known technology context includes Java/Spring Boot, MariaDB, jQuery, pqGrid, Form.io integration, Dynamic REST APIs, and metadata-driven UI generation.
- Verified instance examples include ARK Pharma Returns and SBI FAC HRMS-style/careers/interview-management flows.
- Verified metadata table examples include `jq_module_listing`, `jq_dynamic_form`, `jq_grid_details`, `jq_dynamic_rest_details`, `jq_dynamic_rest_dao_details`, `jq_autocomplete_details`, `jq_additional_datasource`, `jq_file_upload_config`, `jq_dashboard`, `jq_dashlet`, `jq_resource_bundle`, `jq_user`, and `jq_role`.

## Conceptual model
Think of a JQuiver instance in four layers:

1. Platform runtime: Java/Spring Boot application, frontend libraries, metadata execution services, security, schedulers, file serving, and database access.
2. Platform metadata: `jq_` tables that describe routes, pages, APIs, forms, grids, datasources, dashboards, roles, resource bundles, and file bins.
3. Business schema: domain tables and views such as ARK policy/product tables or SBI job/applicant/interview tables.
4. Runtime artifacts: uploaded files, templates, environment configuration, packaged JARs, and instance-specific deployment settings.

## Safe AI-agent usage
- Start from metadata and schema evidence, not assumptions.
- Treat a JQuiver "screen" as a graph of related metadata.
- Redact credentials and private data from summaries.
- Recommend backup before metadata or database changes.
- Mark unknown implementation details with TODOs.

## TODO items to verify
- TODO: verify official JQuiver product terminology from source/product documentation.
- TODO: verify full supported module list from current JQuiver source code.
- TODO: verify exact version compatibility between analyzed exports and current source.

## Example
If a user says "the applicant listing page is broken," an agent should not jump straight to UI code. It should inspect route metadata, grid metadata, datasource configuration, the backing table/view, role permissions, and any Dynamic REST calls used by the page.

