# Troubleshoot Scheduler

## Purpose
Troubleshoot JQuiver scheduled jobs and scheduler-backed Dynamic REST APIs.

## Prerequisites
- Scheduler name or ID identified.
- Read access to scheduler metadata.
- Permission to inspect logs.
- Understanding of target API side effects.

## Inputs required
- Scheduler ID/name: `<SCHEDULER_ID>`.
- Target Dynamic REST API ID/name: `<API_ID>`.
- Cron expression: `<CRON_EXPRESSION>`.
- Environment: `<ENVIRONMENT_NAME>`.
- Symptom: not running, running too often, failed job, wrong side effect, or email issue.

## Steps
1. Inspect `jq_job_scheduler` for the scheduler row.
2. Verify active flag.
3. Verify cron expression.
4. Identify target Dynamic REST API.
5. Inspect target API metadata and side effects.
6. Verify request/header parameters.
7. Check scheduler logs.
8. Check application logs.
9. Check timezone/environment behavior.
10. Check failure notification settings if configured.
11. Do not trigger the job manually unless explicitly authorized.
12. If changes are needed, recommend backup and follow scheduler playbook.

## Validation checklist
- Scheduler row exists.
- Active flag expected.
- Cron expression valid.
- Target API exists.
- Target API side effects understood.
- Logs reviewed.
- Timezone considered.
- Failure notification behavior checked.

## Common errors
- Scheduler disabled.
- Cron expression wrong.
- Target API missing or changed.
- Target API fails because datasource is down.
- Job runs in local/dev when it should not.
- Manual trigger causes production side effect.

## Rollback/safety notes
- Back up scheduler metadata before edits.
- Keep previous cron and active flag for rollback.
- Avoid enabling production jobs outside approved windows.
- If a scheduler caused data changes or email sends, plan operational remediation separately.

## Related KB/reference/playbook files
- `../knowledge-base/14-scheduler.md`
- `../knowledge-base/07-dynamic-rest-api.md`
- `../playbooks/configure-scheduler.md`
- `troubleshoot-dynamic-rest.md`
- `../knowledge-base/27-logging-audit-error-handling.md`

