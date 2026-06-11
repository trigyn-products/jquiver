---
name: jquiver-scheduler
description: Use for JQuiver scheduler analysis, configuration, or troubleshooting involving jq_job_scheduler, jq_job_scheduler_log, Quartz cron expressions, Dynamic REST job targets, cleanup jobs, reminder jobs, timezone, and failure notifications.
---

# JQuiver Scheduler

## 1. Skill name
JQuiver Scheduler

## 2. Purpose
Guide an agent through safe review, configuration, and troubleshooting of JQuiver scheduled jobs.

## 3. Trigger phrases / when to use
- "scheduler"
- "cron"
- "scheduled job"
- "reminder job"
- "cleanup job"
- "jq_job_scheduler"
- "scheduler log"

## 4. Required context
- Scheduler name/ID.
- Target Dynamic REST API.
- Cron expression.
- Environment and timezone.
- Side effects.
- Active/inactive expectation.

## 5. Files to read first
- `../../knowledge-base/14-scheduler.md`
- `../../knowledge-base/07-dynamic-rest-api.md`
- `../../developer-runbook/troubleshoot-scheduler.md`
- `../../playbooks/configure-scheduler.md`
- `../../reference/metadata-table-reference.md`

## 6. Step-by-step workflow
1. Inspect `jq_job_scheduler`.
2. Inspect target `jq_dynamic_rest_details`.
3. Identify side effects such as mail, delete, cleanup, integration calls, or data writes.
4. Verify cron expression and timezone.
5. Check active flag and environment scheduler policy.
6. Inspect scheduler logs.
7. Test target API manually only in an approved non-production context.
8. Enable or change schedules only after backup and approval.

## 7. Output format
Return:
- Scheduler map.
- Cron/timezone details.
- Target API and side effects.
- Active/environment status.
- Log findings.
- Validation and rollback plan.

## 8. Safety rules
- Do not run side-effecting schedulers unless explicitly asked.
- Keep production-like jobs disabled in local/dev unless intentionally testing.
- Verify mail/API side effects before enabling.
- Recommend backup before metadata changes.

## 9. Examples
- HRS includes interview panel reminders, audit reminders, mail jobs, and cleanup jobs.
- SBI FAC includes cleanup and captcha scheduler patterns.

## 10. Things not to do
- Do not enable production schedulers by accident.
- Do not change cron without timezone review.
- Do not ignore scheduler logs.
- Do not assume the target API is harmless.
