# Table to Feature Map

## Purpose
Map JQuiver core/platform table families and custom instance tables to feature areas.

## When to use this file
Use this file during schema analysis, instance documentation, or troubleshooting.

## Related files
- `verified-schema-index.md`
- `jquiver-core-tables.md`
- `metadata-table-reference.md`

## JQuiver Core Table Feature Map
- `jq_*`: JQuiver metadata and platform features such as routing, forms, grids, Dynamic REST, datasource configuration, file upload, dashboards, dashlets, scheduler metadata, templates, resource bundles, users, roles, permissions, script libraries, API clients, notifications, business modules, and tags.
- `qrtz_*`: Quartz scheduler runtime/support tables.
- `flyway_schema_history`: Flyway database migration history.
- `persistent_logins`: Login/session support.
- `mail_schedule`: Mail scheduling support.

## Custom Table Feature Map
ARK custom module tables:
- Non-core ARK tables map to policy, exception, product/manufacturer, hospital, return instruction, email-template mapping, lookup, and supporting ARK business features.

SBI FAC custom module tables:
- Non-core SBI FAC tables map to careers, job details, applicant submission, OTP/history, interview management, candidate/application tracking, dashboards, and lookup features.

HRS custom module tables:
- Non-core HRS tables map to audit, interview management, L&D/training, consultant management, exit/retention/resignation, vendor management, project/PRM, PO/invoice, grievance, IJP, FIS, ICSAT, and related HR business features.

## Safe usage
- Do not include raw SQL dumps, INSERT statements, row data, credentials, or personal/candidate/employee/mail data.
- Mark uncertain custom-table-to-module mappings as TODO.

## TODO items to verify
- TODO: Verify exact custom table inventories and form/grid/API links per current export.

