# Configure Scheduler

## Goal
Configure a JQuiver scheduler job that invokes a Dynamic REST API.

## When to use
Use this playbook for cleanup jobs, reminders, recurring sync, scheduled emails, or scheduled maintenance tasks.

## Required inputs
- Scheduler name.
- Target Dynamic REST API.
- Cron expression.
- Request/header parameters.
- Active/inactive decision.
- Environment policy.
- Side effect description.

## Files/tables/configuration to inspect first
- `jq_job_scheduler`.
- `jq_dynamic_rest_details`.
- Scheduler logs.
- Target API service logic and DAO rows.
- Environment scheduler policy.

## Step-by-step implementation approach
1. Verify target Dynamic REST API exists.
2. Identify API side effects.
3. Confirm scheduler should run in this environment.
4. Define cron expression and timezone expectation.
5. Configure scheduler metadata after backup.
6. Start inactive first if possible.
7. Test target API in non-production.
8. Enable scheduler in controlled environment.
9. Monitor logs after first run.

## Validation checklist
- Scheduler row exists.
- Target API exists.
- Cron expression valid.
- Side effects approved.
- Environment policy followed.
- Logs show expected execution.
- Failure behavior understood.

## Common mistakes
- Enabling production job during testing.
- Wrong cron expression.
- Target API is unsecured or side-effecting unexpectedly.
- Missing request parameters.
- Timezone mismatch.

## Rollback plan
- Set scheduler inactive using approved metadata change.
- Restore previous cron/API target.
- Revert target API changes separately.
- Remediate side effects manually if job already ran.

## Related skills and reference docs
- `../skills/jquiver-scheduler/SKILL.md`
- `../knowledge-base/14-scheduler.md`
- `../developer-runbook/troubleshoot-scheduler.md`
- `../knowledge-base/07-dynamic-rest-api.md`

