# Knowledge Base Map

## Purpose
Act as the navigation guide for AI agents and developers using this JQuiver knowledge base.

Use this file to decide which files to read first for a task. Do not read randomly. Pick the task, read the listed files in order, then verify exact implementation details from actual JQuiver source code, target database schema, exported metadata, or running instance behavior.

## When to use this file
Use this as the starting navigation file for every JQuiver task.

## Related files
- [README.md](README.md)
- [AGENTS.md](AGENTS.md)
- [CONTRIBUTING.md](CONTRIBUTING.md)
- [reference/metadata-table-reference.md](reference/metadata-table-reference.md)
- [reference/ai-agent-review-checklist.md](reference/ai-agent-review-checklist.md)

## Known facts
- Platform concepts are documented under `knowledge-base/`.
- Exact table and naming references are documented under `reference/`.
- Operational procedures are documented under `developer-runbook/`.
- Change workflows are documented under `playbooks/`.
- Agent-specific procedures are documented under `skills/`.
- Example patterns are documented under `examples/`.

## TODO items to verify
- TODO: Link each page to exact source-code packages once source code is reviewed.
- TODO: Add source-specific read paths when JQuiver source modules are mapped.
- TODO: Add version-specific paths if metadata tables differ by JQuiver version.

## Universal First Reads
For any JQuiver task, read these first:

1. `AGENTS.md`
2. `README.md`
3. `reference/ai-agent-review-checklist.md`
4. The task-specific files below

## Task Reading Map

### Create CRUD Module
Read first:
- `knowledge-base/00-overview.md`
- `knowledge-base/02-core-database-model.md`
- `knowledge-base/03-module-router.md`
- `knowledge-base/04-form-builder.md`
- `knowledge-base/06-grid-utils.md`
- `knowledge-base/07-dynamic-rest-api.md`
- `knowledge-base/15-security-users-roles.md`
- `reference/metadata-table-reference.md`
- `reference/naming-conventions.md`
- `playbooks/create-end-to-end-crud-module.md`
- `playbooks/create-module-route.md`
- `playbooks/create-form-builder-form.md`
- `playbooks/create-grid.md`

Then verify:
- Target business table/schema.
- Route metadata.
- Form metadata.
- Grid metadata.
- Role/menu access.
- Backup and rollback plan.

### Modify Existing Module
Read first:
- `knowledge-base/24-jquiver-page-lifecycle.md`
- `knowledge-base/30-ai-agent-working-rules.md`
- `reference/metadata-table-reference.md`
- `reference/ai-agent-review-checklist.md`
- `developer-runbook/jquiver-metadata-navigation.md`
- `developer-runbook/database-backup-restore.md`
- `playbooks/modify-existing-module.md`

Then verify:
- Existing route, target metadata, datasource, permissions, and dependencies.
- Whether the metadata is system-owned or custom.

### Create Form Builder Form
Read first:
- `knowledge-base/04-form-builder.md`
- `knowledge-base/03-module-router.md`
- `knowledge-base/13-additional-datasource.md`
- `reference/metadata-table-reference.md`
- `developer-runbook/troubleshoot-forms.md`
- `playbooks/create-form-builder-form.md`
- `skills/jquiver-form-builder/SKILL.md`

Then verify:
- `jq_dynamic_form`.
- `jq_dynamic_form_save_queries`.
- Target datasource and query type.
- Captcha and CSRF behavior.

### Create Form.io Pluggable Form
Read first:
- `knowledge-base/05-formio-pluggable-forms.md`
- `knowledge-base/20-external-form-embedding.md`
- `knowledge-base/25-authentication-authorization-flow.md`
- `playbooks/create-formio-pluggable-form.md`
- `playbooks/embed-formio-external-app.md`
- `skills/jquiver-formio/SKILL.md`
- `examples/formio-embed-single-submit.html`
- `examples/formio-embed-multi-submit.html`

Then verify:
- Form.io metadata tables.
- Single-submit or multi-submit behavior.
- Anonymous/public access requirements.
- Frame and security headers.

### Create Grid/Listing Page
Read first:
- `knowledge-base/06-grid-utils.md`
- `knowledge-base/03-module-router.md`
- `knowledge-base/13-additional-datasource.md`
- `knowledge-base/29-performance-guidelines.md`
- `reference/metadata-table-reference.md`
- `developer-runbook/troubleshoot-grids.md`
- `playbooks/create-grid.md`
- `skills/jquiver-grid-utils/SKILL.md`
- `examples/grid-examples.md`

Then verify:
- `jq_grid_details`.
- Table or view existence.
- Column list.
- Custom filter criteria.
- Datasource ID.
- Pagination and permissions.

### Create Dynamic REST API
Read first:
- `knowledge-base/07-dynamic-rest-api.md`
- `knowledge-base/13-additional-datasource.md`
- `knowledge-base/25-authentication-authorization-flow.md`
- `reference/metadata-table-reference.md`
- `developer-runbook/troubleshoot-dynamic-rest.md`
- `playbooks/create-dynamic-rest-api.md`
- `skills/jquiver-dynamic-rest/SKILL.md`
- `examples/dynamic-rest-examples.md`

Then verify:
- `jq_dynamic_rest_details`.
- `jq_dynamic_rest_dao_details`.
- Request type.
- Response producer.
- Security flag.
- Side effects before invocation.

### Configure Additional Datasource
Read first:
- `knowledge-base/13-additional-datasource.md`
- `knowledge-base/28-integration-patterns.md`
- `reference/environment-config-reference.md`
- `reference/metadata-table-reference.md`
- `developer-runbook/troubleshoot-datasource.md`
- `playbooks/configure-additional-datasource.md`
- `skills/jquiver-additional-datasource/SKILL.md`
- `examples/additional-datasource-examples.md`

Then verify:
- `jq_additional_datasource`.
- `jq_datasource_lookup`.
- Credential handling.
- Connection validation.
- Consumers using `datasource_id`.

### Configure File Upload
Read first:
- `knowledge-base/09-file-upload.md`
- `knowledge-base/15-security-users-roles.md`
- `reference/security-permission-matrix.md`
- `developer-runbook/troubleshoot-file-upload.md`
- `developer-runbook/safe-data-handling.md`
- `playbooks/configure-file-upload-bin.md`
- `skills/jquiver-file-upload/SKILL.md`

Then verify:
- `jq_file_upload_config`.
- `jq_file_upload`.
- File bin ID.
- Allowed extensions.
- Max size and file count.
- Physical storage behavior.
- Download permissions.

### Configure Scheduler
Read first:
- `knowledge-base/14-scheduler.md`
- `knowledge-base/07-dynamic-rest-api.md`
- `knowledge-base/27-logging-audit-error-handling.md`
- `reference/metadata-table-reference.md`
- `developer-runbook/troubleshoot-scheduler.md`
- `playbooks/configure-scheduler.md`
- `skills/jquiver-scheduler/SKILL.md`

Then verify:
- `jq_job_scheduler`.
- Dynamic REST target.
- Cron expression.
- Timezone.
- Side effects.
- Logs and failure notification settings.

### Configure Dashboard/Dashlet
Read first:
- `knowledge-base/11-dashboards-and-dashlets.md`
- `knowledge-base/13-additional-datasource.md`
- `knowledge-base/29-performance-guidelines.md`
- `reference/metadata-table-reference.md`
- `playbooks/create-dashboard-and-dashlet.md`
- `skills/jquiver-dashboard-dashlet/SKILL.md`
- `examples/dashboard-dashlet-examples.md`

Then verify:
- `jq_dashboard`.
- `jq_dashlet`.
- Dashboard/dashlet association metadata.
- Query performance.
- Datasource ID.
- Permissions.

### Configure Multilingual Labels
Read first:
- `knowledge-base/12-multilingual-resource-bundles.md`
- `reference/naming-conventions.md`
- `reference/ui-standards.md`
- `playbooks/configure-multilingual-labels.md`
- `skills/jquiver-multilingual/SKILL.md`

Then verify:
- `jq_resource_bundle`.
- `jq_module_listing_i18n`.
- Language IDs.
- Fallback behavior.
- UI rendering.

### Configure Autocomplete/Typeahead
Read first:
- `knowledge-base/31-autocomplete-typeahead.md`
- `knowledge-base/13-additional-datasource.md`
- `reference/metadata-table-reference.md`
- `playbooks/configure-autocomplete-typeahead.md`
- `skills/jquiver-autocomplete-typeahead/SKILL.md`
- `examples/autocomplete-typeahead-examples.md`

Then verify:
- `jq_autocomplete_details`.
- Query and datasource ID.
- Request parameter names.
- Response field shape.
- Form/template usage.

### Configure Template, Email XML, Or Webclient XML
Read first:
- `knowledge-base/08-templates.md`
- `knowledge-base/17-notifications-mail.md`
- `knowledge-base/07-dynamic-rest-api.md`
- `reference/metadata-table-reference.md`
- `playbooks/configure-template-email-webclient.md`
- `skills/jquiver-template-mail/SKILL.md`

Then verify:
- `jq_template_master`.
- Template consumers.
- Response producer type.
- Email/XML or webclient XML schema.
- Merge variables and escaping.
- Side effects before sending mail or calling external services.

### Configure Script Library
Read first:
- `knowledge-base/32-script-library.md`
- `knowledge-base/07-dynamic-rest-api.md`
- `knowledge-base/08-templates.md`
- `reference/metadata-table-reference.md`
- `playbooks/configure-script-library.md`
- `skills/jquiver-script-library/SKILL.md`

Then verify:
- `jq_script_lib_details`.
- `jq_script_lib_connect`.
- Attached entity IDs.
- Script type.
- Runtime scope and all consumers.

### Configure API Client
Read first:
- `knowledge-base/33-api-clients.md`
- `knowledge-base/07-dynamic-rest-api.md`
- `knowledge-base/25-authentication-authorization-flow.md`
- `knowledge-base/28-integration-patterns.md`
- `reference/security-permission-matrix.md`
- `playbooks/configure-api-client.md`
- `skills/jquiver-api-client/SKILL.md`

Then verify:
- `jq_api_client_details`.
- Client key/secret handling.
- Inclusion URL pattern.
- Encryption settings.
- Target API security and permissions.

### Define Home Page
Read first:
- `knowledge-base/03-module-router.md`
- `knowledge-base/24-jquiver-page-lifecycle.md`
- `knowledge-base/15-security-users-roles.md`
- `reference/security-permission-matrix.md`
- `playbooks/configure-home-page.md`
- `skills/jquiver-security-user-management/SKILL.md`

Then verify:
- `jq_module_listing`.
- Existing `is_home_page` values.
- Target route target type.
- Role access and route parameters.
- Layout behavior.

### Configure Notification Or Mail
Read first:
- `knowledge-base/17-notifications-mail.md`
- `knowledge-base/08-templates.md`
- `knowledge-base/14-scheduler.md`
- `knowledge-base/07-dynamic-rest-api.md`
- `developer-runbook/troubleshoot-scheduler.md`
- `playbooks/configure-notification-mail.md`
- `skills/jquiver-notification-mail/SKILL.md`

Then verify:
- `jq_generic_user_notification`.
- Mail/template metadata for the target instance.
- Audience selection criteria.
- Scheduler/API target.
- Test recipient or limited-audience plan.

### Configure Business Module Or Tags
Read first:
- `knowledge-base/34-business-modules-and-tags.md`
- `knowledge-base/16-import-export-versioning.md`
- `reference/metadata-table-reference.md`
- `playbooks/configure-business-module-and-tags.md`
- `skills/jquiver-business-module-tags/SKILL.md`

Then verify:
- `jq_business_module`.
- `jq_business_module_entity_details`.
- `jq_tag_data`.
- `jq_tag_entity_details`.
- Referenced forms, grids, APIs, templates, and permissions.

### Review Application Configuration
Read first:
- `knowledge-base/35-application-configuration.md`
- `knowledge-base/26-deployment-and-environment.md`
- `reference/environment-config-reference.md`
- `developer-runbook/environment-configuration.md`
- `developer-runbook/safe-data-handling.md`
- `skills/jquiver-application-configuration/SKILL.md`

Then verify:
- User authorization to read sensitive config files.
- Exact source/config property keys.
- Authentication mode.
- Mail settings.
- Scheduler base URL.
- Upload location.
- Security settings.

### Analyze Existing Instance
Read first:
- `knowledge-base/00-overview.md`
- `knowledge-base/02-core-database-model.md`
- `knowledge-base/23-jquiver-metadata-tables-reference.md`
- `developer-runbook/local-analysis-runbook.md`
- `developer-runbook/database-schema-analysis.md`
- `developer-runbook/jquiver-metadata-navigation.md`
- `developer-runbook/safe-data-handling.md`
- `playbooks/analyze-existing-instance.md`
- `skills/jquiver-instance-analyzer/SKILL.md`

Instance-specific examples:
- `knowledge-base/18-ark-pharma-instance.md`
- `knowledge-base/19-sbi-fac-instance.md`
- `examples/ark-instance-summary.md`
- `examples/sbi-fac-instance-summary.md`
- `examples/hrs-instance-summary.md`

Then verify:
- SQL files.
- Tables and views.
- Row counts.
- Metadata routes.
- Forms, grids, APIs, datasources, schedulers, dashboards, file bins, uploads.
- PII and credential redaction.

### Debug Production Issue
Read first:
- `knowledge-base/24-jquiver-page-lifecycle.md`
- `knowledge-base/27-logging-audit-error-handling.md`
- `knowledge-base/30-ai-agent-working-rules.md`
- `reference/ai-agent-review-checklist.md`
- `developer-runbook/common-sql-diagnostics.md`
- `developer-runbook/safe-data-handling.md`
- `playbooks/debug-production-issue.md`

Then add module-specific files:
- Form issue: `developer-runbook/troubleshoot-forms.md`
- Grid issue: `developer-runbook/troubleshoot-grids.md`
- Dynamic REST issue: `developer-runbook/troubleshoot-dynamic-rest.md`
- Datasource issue: `developer-runbook/troubleshoot-datasource.md`
- File upload issue: `developer-runbook/troubleshoot-file-upload.md`
- Scheduler issue: `developer-runbook/troubleshoot-scheduler.md`

Then verify:
- Symptom.
- Impacted route/page/API.
- Recent metadata or deployment changes.
- Logs.
- Read-only SQL diagnostics.
- Backup and rollback plan before any write.

### Configure Help Manual
Read first:
- `knowledge-base/10-help-manual.md`
- `knowledge-base/09-file-upload.md`
- `reference/metadata-table-reference.md`
- `playbooks/configure-help-manual.md`
- `skills/jquiver-help-manual/SKILL.md`

Then verify:
- Exact help manual tables.
- Module association.
- `helpManual` file bin behavior.
- Asset visibility.

### Create Reporting Page
Read first:
- `knowledge-base/06-grid-utils.md`
- `knowledge-base/11-dashboards-and-dashlets.md`
- `knowledge-base/29-performance-guidelines.md`
- `playbooks/create-reporting-page.md`
- `playbooks/create-grid.md`
- `playbooks/create-dashboard-and-dashlet.md`

Then verify:
- Whether the report should be a grid, dashboard, dashlet, template page, or export.
- Query performance and indexing.
- Security and data sensitivity.

### Migrate Instance Configuration
Read first:
- `knowledge-base/16-import-export-versioning.md`
- `knowledge-base/13-additional-datasource.md`
- `knowledge-base/09-file-upload.md`
- `developer-runbook/database-backup-restore.md`
- `developer-runbook/release-and-deployment-runbook.md`
- `playbooks/migrate-instance-configuration.md`

Then verify:
- Source and target JQuiver versions.
- Platform metadata dependencies.
- Custom schema dependencies.
- Additional datasource IDs.
- Upload folders.
- Rollback plan.

## Example
For Dynamic REST work, read:

1. `AGENTS.md`
2. `knowledge-base/07-dynamic-rest-api.md`
3. `reference/metadata-table-reference.md`
4. `developer-runbook/troubleshoot-dynamic-rest.md`
5. `playbooks/create-dynamic-rest-api.md`
6. `skills/jquiver-dynamic-rest/SKILL.md`

Then verify the target instance metadata before producing SQL or implementation steps.

## Generating Agent-Specific Adapter Files

This KB is agent-neutral. Do not maintain adapter files for every AI tool by default.

When a target AI agent is selected, generate only the required thin adapter file for that tool. Examples:
- `CLAUDE.md` for Claude Code.
- `GEMINI.md` for Gemini CLI.
- `.github/copilot-instructions.md` for GitHub Copilot.
- `.cursor/rules/jquiver.mdc` for Cursor.

The adapter must not duplicate the full KB. It should point the agent to:
1. `README.md`
2. `AGENTS.md`
3. `KB-MAP.md`
4. `reference/jquiver-core-tables.md`
5. `knowledge-base/30-ai-agent-working-rules.md`
6. Relevant playbook under `/playbooks`
7. Relevant skill under `/skills`

Adapter rules:
- Use minimum context.
- Do not read the full KB unless required.
- Do not modify JQuiver core tables directly.
- Core tables are `jq_*`, `flyway_schema_history`, `mail_schedule`, `persistent_logins`, and `qrtz_*`.
- All other tables are custom module/business tables.
- Mark assumptions clearly.
- Do not generate destructive SQL without explicit approval.
- Recommend backup before metadata or database changes.
