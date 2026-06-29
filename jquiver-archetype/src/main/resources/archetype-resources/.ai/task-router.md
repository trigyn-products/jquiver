# JQuiver AI Task Router

Use this file to decide which full KB files should be read for a given task.

The full KB is expected at:

```text
.ai/kb/
```

## Always read first

```text
.ai/kb/AGENTS.md
.ai/kb/KB-MAP.md
.ai/kb/reference/ai-agent-review-checklist.md
```

## Analyze existing instance

Use for Ark Pharma, SBI FAC, HRS, or any metadata/database/source-code review.

```text
.ai/kb/skills/jquiver-instance-analyzer/SKILL.md
.ai/kb/playbooks/analyze-existing-instance.md
.ai/kb/developer-runbook/local-analysis-runbook.md
.ai/kb/developer-runbook/database-schema-analysis.md
.ai/kb/developer-runbook/jquiver-metadata-navigation.md
.ai/kb/reference/metadata-table-reference.md
.ai/kb/examples/ark-instance-summary.md
.ai/kb/examples/sbi-fac-instance-summary.md
.ai/kb/examples/hrs-instance-summary.md
```

## Create CRUD or business module

```text
.ai/kb/playbooks/create-end-to-end-crud-module.md
.ai/kb/playbooks/create-module-route.md
.ai/kb/playbooks/create-form-builder-form.md
.ai/kb/playbooks/create-grid.md
.ai/kb/playbooks/add-role-based-menu-access.md
.ai/kb/knowledge-base/02-core-database-model.md
.ai/kb/knowledge-base/03-module-router.md
.ai/kb/knowledge-base/04-form-builder.md
.ai/kb/knowledge-base/06-grid-utils.md
.ai/kb/knowledge-base/07-dynamic-rest-api.md
.ai/kb/knowledge-base/24-jquiver-page-lifecycle.md
```

## Modify existing module

```text
.ai/kb/playbooks/modify-existing-module.md
.ai/kb/developer-runbook/jquiver-metadata-navigation.md
.ai/kb/knowledge-base/23-jquiver-metadata-tables-reference.md
.ai/kb/knowledge-base/30-ai-agent-working-rules.md
```

## Form Builder

```text
.ai/kb/skills/jquiver-form-builder/SKILL.md
.ai/kb/playbooks/create-form-builder-form.md
.ai/kb/developer-runbook/troubleshoot-forms.md
.ai/kb/knowledge-base/04-form-builder.md
```

## Form.io default, custom, or pluggable form

```text
.ai/kb/skills/jquiver-formio/SKILL.md
.ai/kb/playbooks/create-formio-pluggable-form.md
.ai/kb/playbooks/embed-formio-external-app.md
.ai/kb/knowledge-base/05-formio-pluggable-forms.md
.ai/kb/knowledge-base/20-external-form-embedding.md
.ai/kb/examples/formio-embed-single-submit.html
.ai/kb/examples/formio-embed-multi-submit.html
```

## Grid or listing page

```text
.ai/kb/skills/jquiver-grid-utils/SKILL.md
.ai/kb/playbooks/create-grid.md
.ai/kb/developer-runbook/troubleshoot-grids.md
.ai/kb/knowledge-base/06-grid-utils.md
.ai/kb/examples/grid-examples.md
```

## Dynamic REST API

Use for Java, FTL, Python, Nashorn/server-side JavaScript, secured REST API, and generated service metadata.

```text
.ai/kb/skills/jquiver-dynamic-rest/SKILL.md
.ai/kb/playbooks/create-dynamic-rest-api.md
.ai/kb/developer-runbook/troubleshoot-dynamic-rest.md
.ai/kb/knowledge-base/07-dynamic-rest-api.md
.ai/kb/examples/dynamic-rest-examples.md
```

## Additional datasource

```text
.ai/kb/skills/jquiver-additional-datasource/SKILL.md
.ai/kb/playbooks/configure-additional-datasource.md
.ai/kb/developer-runbook/troubleshoot-datasource.md
.ai/kb/knowledge-base/13-additional-datasource.md
.ai/kb/examples/additional-datasource-examples.md
```

## File bins and custom file bins

```text
.ai/kb/skills/jquiver-file-upload/SKILL.md
.ai/kb/playbooks/configure-file-upload-bin.md
.ai/kb/developer-runbook/troubleshoot-file-upload.md
.ai/kb/knowledge-base/09-file-upload.md
```

## Autocomplete and typeahead

Use for single-select autocomplete, multiselect autocomplete, and dependent autocomplete.

```text
.ai/kb/skills/jquiver-autocomplete-typeahead/SKILL.md
.ai/kb/playbooks/configure-autocomplete-typeahead.md
.ai/kb/knowledge-base/31-autocomplete-typeahead.md
.ai/kb/examples/autocomplete-typeahead-examples.md
```

## Templates, email XML, and webclient XML

```text
.ai/kb/skills/jquiver-template-mail/SKILL.md
.ai/kb/playbooks/configure-template-email-webclient.md
.ai/kb/knowledge-base/08-templates.md
.ai/kb/knowledge-base/17-notifications-mail.md
```

## Dashboard and dashlet

```text
.ai/kb/skills/jquiver-dashboard-dashlet/SKILL.md
.ai/kb/playbooks/create-dashboard-and-dashlet.md
.ai/kb/knowledge-base/11-dashboards-and-dashlets.md
.ai/kb/examples/dashboard-dashlet-examples.md
```

## Scheduler

```text
.ai/kb/skills/jquiver-scheduler/SKILL.md
.ai/kb/playbooks/configure-scheduler.md
.ai/kb/developer-runbook/troubleshoot-scheduler.md
.ai/kb/knowledge-base/14-scheduler.md
```

## Multilingual resource bundle

```text
.ai/kb/skills/jquiver-multilingual/SKILL.md
.ai/kb/playbooks/configure-multilingual-labels.md
.ai/kb/knowledge-base/12-multilingual-resource-bundles.md
```

## Help manual

```text
.ai/kb/skills/jquiver-help-manual/SKILL.md
.ai/kb/playbooks/configure-help-manual.md
.ai/kb/knowledge-base/10-help-manual.md
```

## Notification and mail

```text
.ai/kb/skills/jquiver-notification-mail/SKILL.md
.ai/kb/playbooks/configure-notification-mail.md
.ai/kb/knowledge-base/17-notifications-mail.md
```

## Script library

```text
.ai/kb/skills/jquiver-script-library/SKILL.md
.ai/kb/playbooks/configure-script-library.md
.ai/kb/knowledge-base/32-script-library.md
```

## API clients

```text
.ai/kb/skills/jquiver-api-client/SKILL.md
.ai/kb/playbooks/configure-api-client.md
.ai/kb/knowledge-base/33-api-clients.md
```

## Business modules and tags

```text
.ai/kb/skills/jquiver-business-module-tags/SKILL.md
.ai/kb/playbooks/configure-business-module-and-tags.md
.ai/kb/knowledge-base/34-business-modules-and-tags.md
```

## User management, authentication, roles, and permissions

```text
.ai/kb/skills/jquiver-security-user-management/SKILL.md
.ai/kb/playbooks/add-role-based-menu-access.md
.ai/kb/knowledge-base/15-security-users-roles.md
.ai/kb/knowledge-base/25-authentication-authorization-flow.md
.ai/kb/reference/security-permission-matrix.md
```

## Application configuration and home page

```text
.ai/kb/skills/jquiver-application-configuration/SKILL.md
.ai/kb/playbooks/configure-home-page.md
.ai/kb/developer-runbook/environment-configuration.md
.ai/kb/reference/environment-config-reference.md
.ai/kb/knowledge-base/35-application-configuration.md
```

## Troubleshooting production or local issues

```text
.ai/kb/playbooks/debug-production-issue.md
.ai/kb/developer-runbook/common-sql-diagnostics.md
.ai/kb/developer-runbook/safe-data-handling.md
.ai/kb/knowledge-base/27-logging-audit-error-handling.md
.ai/kb/knowledge-base/29-performance-guidelines.md
```

## Deployment and environment

```text
.ai/kb/developer-runbook/setup-local-development.md
.ai/kb/developer-runbook/build-and-run-jquiver.md
.ai/kb/developer-runbook/environment-configuration.md
.ai/kb/developer-runbook/release-and-deployment-runbook.md
.ai/kb/knowledge-base/26-deployment-and-environment.md
```
