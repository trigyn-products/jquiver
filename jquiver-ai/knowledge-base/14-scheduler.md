# Scheduler

## Purpose
Explain JQuiver scheduler metadata and scheduled Dynamic REST execution.

## When to use this file
Use this file when configuring or troubleshooting cleanup jobs, reminders, scheduled integrations, or scheduled email workflows.

## Related files
- `07-dynamic-rest-api.md`
- `27-logging-audit-error-handling.md`
- `../playbooks/configure-scheduler.md`
- `../developer-runbook/troubleshoot-scheduler.md`

## Known facts
- Observed scheduler metadata uses `jq_job_scheduler`.
- Scheduler rows reference Dynamic REST API IDs.
- SBI FAC includes system schedulers such as notification cleanup, temp file cleanup, scheduler log cleanup, version checking, and captcha cleanup.

## Scheduler lifecycle
1. Scheduler metadata defines job name, active flag, cron expression, target API, and parameters.
2. Runtime scheduler triggers the configured job.
3. Target Dynamic REST API executes.
4. Logs and failure notification behavior may record outcome.

## Relationships
- Scheduler -> Dynamic REST API.
- Scheduler -> cron expression.
- Scheduler -> request/header parameters.
- Scheduler -> logs and failure notifications.
- Scheduler -> side effects such as deletion, email, cleanup, or external integration.

## Safe AI-agent usage
- Do not manually trigger scheduler-backed APIs unless the user explicitly asks.
- Identify side effects before testing.
- Verify timezone and cron syntax.
- Disable/enable jobs only with approval.
- Recommend backup before modifying scheduler metadata.

## TODO items to verify
- TODO: verify Quartz table usage and scheduler lifecycle.
- TODO: verify cron expression format and timezone behavior.
- TODO: verify failed notification behavior.
- TODO: verify scheduler clustering behavior.

## Example
A captcha cleanup scheduler may be safe conceptually, but an agent must still verify the target API and affected tables before execution.

