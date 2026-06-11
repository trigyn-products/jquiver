# HRS Instance Summary

## Status
Sample instance summary verified from the supplied `hrsdev.sql` export at schema/table-name level. Runtime behavior, source-code behavior, and business rules must still be verified from the running application or source code before use.

## Purpose
Show how to summarize a large JQuiver HRMS-style instance while avoiding credentials, private employee/candidate data, and production-specific configuration.

## Sample Summary

Instance family:
- HRS / HRMS-style JQuiver implementation.

Observed database:
- Export database name: `hrsdev`.
- SQL export size was large, roughly 453 MB decimal / 433 MiB.
- Observed DB object count: 473 base tables and 116 views, 589 total unique DB objects.

Observed module areas supported by table names in `hrsdev.sql`:
- Audit management.
- Interview management.
- L&D / training.
- Consultant management.
- Exit / retention / resignation.
- Vendor management.
- Project / PRM.
- PO / invoice.
- Grievance.
- IJP.
- FIS.
- ICSAT.
- Dashboards / dashlets.
- Dynamic REST.
- Scheduler.
- File upload.
- Autocomplete.
- Script library.
- API clients.
- Business modules / tags.

Observed JQuiver capabilities:
- Routes and menu metadata.
- Form Builder forms.
- Grid Utils listings.
- Dynamic REST APIs.
- Additional datasource metadata.
- Autocomplete/typeahead metadata.
- File upload bins.
- Scheduler jobs and scheduler logs.
- Dashboards and dashlets.
- Form.io metadata.
- Script libraries.
- API clients.
- Business modules and tags.
- Notifications and mail-oriented scheduler/API flows.

JQuiver platform metadata/core tables used by the instance:
- `jq_*` metadata tables for routes, forms, grids, Dynamic REST, additional datasources, autocompletes, file bins, schedulers, dashboards, Form.io, script libraries, API clients, business modules, tags, notifications, users, roles, and permissions.
- `flyway_schema_history` for migrations, `mail_schedule` for mail scheduling, `persistent_logins` for login/session support, and `qrtz_*` for Quartz scheduler support when present in the export.

Custom business/module tables created for this instance:
- Non-core tables supporting audit, interview management, L&D/training, consultant management, exit/retention/resignation, vendor management, project/PRM, PO/invoice, grievance, IJP, FIS, ICSAT, and related HR workflows.
- Treat every non-`jq_*` table as custom unless it is `flyway_schema_history`, `mail_schedule`, `persistent_logins`, or `qrtz_*`.

Observed integration pattern:
- Additional datasource rows used JDBC-style database integrations.
- CosmosDB-style activity log behavior appeared in Dynamic REST service logic, not as a normal JDBC additional datasource row.
- Activity-log-related Dynamic REST methods included examples such as `logActivity`, `getActivityLog`, `activityLog`, and `getTopUsageActivityLog`.

Observed dashboard/dashlet pattern:
- HRS included a metadata/dashboard-oriented dashboard and dashlets such as active modules, active users, role-based users, and top interviewers.

Sensitive areas:
- Employee data.
- Candidate/applicant data.
- Resume or uploaded documents.
- Email recipient lists and mail bodies.
- Datasource credentials.
- API client keys/secrets.
- Scheduler logs containing URLs or stack traces.

Safe notes:
- Do not expose credentials from `jq_additional_datasource` or `jq_api_client_details`.
- Do not print raw candidate, employee, resume, OTP, or mail content.
- Do not read environment config files unless explicitly authorized.
- Do not call scheduler or mail Dynamic REST APIs during analysis.
- Do not include raw row data, employee data, candidate data, mail bodies, credentials, API secrets, or datasource passwords.
- Keep summaries at schema level; do not expand large SQL content into markdown.

Suggested agent output shape:

```text
Instance: HRS sample
Database: hrsdev
Major modules: audit, interview management, L&D, consultant, exit/separation, vendor, project/PRM, PO, grievance, IJP, FIS, ICSAT
Key JQuiver features: routes, forms, grids, Dynamic REST, datasources, autocomplete, file bins, scheduler, dashboards, Form.io, script libraries, API clients, business modules, tags
Sensitive areas: employee/candidate data, uploaded files, datasource credentials, API client secrets, mail data
Open TODOs: verify current export, route permissions, runtime behavior, scheduler side effects, source-level integration details
```
