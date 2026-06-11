# SBI FAC Instance Summary

## Status
Sample instance summary based on observed exported metadata and service-layer behavior. This is not a final factual document and must be verified against the current SBI FAC export, database, source code, and running instance before use.

## Purpose
Show how to summarize a JQuiver HRMS/careers-style instance without exposing candidate or applicant data.

## Sample Summary

Instance family:
- SBI FAC careers/HRMS-style JQuiver implementation.

Observed schema pattern:
- Main JQuiver/platform schema for metadata and HR workflows.
- Custom careers schema for job details, applicant details, OTP/history, and lookup tables.

Observed module areas:
- Public careers page.
- Job listing and job detail.
- Applicant submission flow.
- Interview management.
- Candidate/application tracking.
- Dashboards and scheduled jobs.

Observed JQuiver capabilities:
- Template-backed public pages.
- Metadata-driven forms, grids, and Dynamic REST APIs.
- File bin for applicant resume uploads.
- Scheduler and cleanup jobs.
- Interview module with domain tables and route/API metadata.

JQuiver platform metadata/core tables used by the instance:
- `jq_*` metadata tables for routes, templates, forms, grids, Dynamic REST, file bins, schedulers, dashboards, users, roles, and permissions.
- Core support tables such as `flyway_schema_history`, `mail_schedule`, `persistent_logins`, and `qrtz_*` if present in the export.

Custom business/module tables created for this instance:
- Non-core tables for careers, job details, applicant details, OTP/history, interview management, candidate/application tracking, and lookup data.
- Treat every non-`jq_*` table as custom unless it is `flyway_schema_history`, `mail_schedule`, `persistent_logins`, or `qrtz_*`.

Safety notes:
- Applicant resumes, emails, phone numbers, OTP data, and candidate JSON are sensitive.
- Public route behavior must be verified from route permissions and running instance behavior.
- Chrome/browser access issues should not block read-only SQL/service-layer analysis.
- Do not include raw row data, credentials, resume content, applicant data, candidate data, OTP values, or mail content.

Suggested agent output shape:

```text
Instance: SBI FAC sample
Major modules: public careers, job details, applicant flow, interview management
Key JQuiver features: templates, forms, grids, Dynamic REST, file bins, schedulers, dashboards
Sensitive areas: applicant/candidate data, resumes, OTP/history, user data
Open TODOs: verify current route permissions, schema names, upload storage, API side effects
```
