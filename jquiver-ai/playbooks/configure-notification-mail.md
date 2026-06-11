# Configure Notification And Mail

## Goal
Configure in-app notification or mail behavior safely.

## When to use
Use this playbook when creating notifications, email templates, reminder jobs, or mail-triggering Dynamic REST APIs.

## Required inputs
- Notification or email use case.
- Audience/recipient rules.
- Validity dates for notifications.
- Template name or email/XML template.
- Dynamic REST API or scheduler target, if any.
- Environment mail configuration owner.

## Files/tables/configuration to inspect first
- `jq_generic_user_notification`.
- Email/template metadata for the target instance.
- `jq_dynamic_rest_details`.
- `jq_job_scheduler`.
- Mail history/failure tables for the target version.
- `knowledge-base/17-notifications-mail.md`.
- `knowledge-base/08-templates.md`.

## Step-by-step implementation approach
1. Decide whether the need is in-app notification, email, or both.
2. Verify audience selection criteria using read-only queries.
3. Verify template merge fields and sample data.
4. Confirm mail/environment configuration with the instance owner.
5. Back up current notification/template/API/scheduler metadata.
6. Configure metadata in non-production first.
7. Test with a limited audience or test mailbox.
8. Confirm failure handling and logs.
9. Enable scheduler or production audience only after approval.

## Validation checklist
- Audience is correct.
- Message validity window is correct.
- Display-once behavior is understood.
- Template renders with real merge data.
- Test mail/notification reaches only intended recipients.
- Failure logs are reviewed.

## Common mistakes
- Triggering production emails during analysis.
- Using broad audience criteria.
- Exposing recipient lists or private message bodies.
- Enabling a scheduler before the API target is validated.

## Rollback plan
- Disable scheduler or notification.
- Restore previous template/API metadata.
- Revert audience criteria.
- Notify owners if unintended messages were sent.

## Related skills and reference docs
- `../knowledge-base/17-notifications-mail.md`
- `../knowledge-base/08-templates.md`
- `../knowledge-base/14-scheduler.md`
- `../developer-runbook/troubleshoot-scheduler.md`
