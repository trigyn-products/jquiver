# Logging, Audit, and Error Handling

## Purpose
Explain logging, audit, and error-handling concepts for JQuiver troubleshooting.

## When to use this file
Use this file during production support, Dynamic REST debugging, scheduler troubleshooting, form/grid errors, and deployment validation.

## Related files
- `07-dynamic-rest-api.md`
- `14-scheduler.md`
- `17-notifications-mail.md`
- `../playbooks/debug-production-issue.md`
- `../developer-runbook/common-sql-diagnostics.md`

## Known facts
- Scheduler logs and mail history tables were present in analyzed exports.
- Metadata rows often carry created/updated by and timestamp columns.
- Dynamic REST APIs and scheduler jobs can produce side effects that need auditability.

## Troubleshooting layers
- Browser/UI console errors.
- Service-layer HTTP status and response payload.
- Application logs.
- Dynamic REST metadata and service logic.
- DAO query errors.
- Scheduler logs.
- Mail history/failure logs.
- Database constraints and view errors.

## Audit concepts
Audit data may come from:
- Metadata timestamps.
- User IDs on created/updated fields.
- Mail history.
- Scheduler execution history.
- Domain table audit columns.
- Application logs.

## Safe AI-agent usage
- Prefer read-only diagnostics first.
- Do not expose private log contents.
- Redact emails, tokens, OTPs, and payloads.
- Do not clear logs unless explicitly asked.
- Tie every error to route/API/form/grid metadata where possible.

## TODO items to verify
- TODO: verify application log locations and logger configuration.
- TODO: verify audit table names and retention policies.
- TODO: verify error response format for Dynamic REST APIs.
- TODO: verify scheduler log table behavior.

## Example
For a failed scheduled email, inspect scheduler metadata, target Dynamic REST API, email template, mail configuration, mail history, and application logs.

