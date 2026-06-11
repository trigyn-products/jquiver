# Notifications and Mail

## Purpose
Explain JQuiver notification and email concepts at a safe v1 level.

## When to use this file
Use this file when working with email templates, mail history, failed mail handling, notification cleanup, reminder jobs, or scheduler-backed email flows.

## Related files
- `14-scheduler.md`
- `27-logging-audit-error-handling.md`
- `08-templates.md`
- `../developer-runbook/troubleshoot-scheduler.md`

## Known facts
- ARK includes `ark_email_template` and `ark_policy_email_template_map`.
- Observed scheduler names include notification cleanup.
- Verified exports include mail-related metadata/history tables such as `jq_template_master`, `jq_failed_mail_history`, and `jq_mail_history_data`.
- `jq_sendMail` is not found as a table in the verified exports; do not document it as a table.
- Some Dynamic REST response producer types observed in exports include email/XML.
- Observed HRS metadata includes `jq_generic_user_notification`.
- Observed HRS notification fields include validity window, message text, message type, selection criteria, display-once flag, target platform, and optional datasource ID.
- HRS includes mail/scheduler examples for interview reminders, exit interview reminders, audit reminders, L&D assessment mail, IJP publish mail, and cleanup jobs.

## Conceptual behavior
JQuiver mail workflows can be platform-level or instance-specific. A mail workflow may involve a template, merge data, scheduler, Dynamic REST API, service logic, mail history, and retry/failure tracking.

## Notification
- Use notification metadata for in-app or user-targeted messages.
- Validate message validity dates, display-once behavior, audience selection criteria, and target platform.
- If selection criteria uses a datasource, verify the query and datasource access.
- Avoid broad notifications until audience filtering is tested.

## Mail configuration
- Treat SMTP host, username, password, sender, TLS, and retry settings as environment-specific.
- Do not store real secrets in documentation.
- Prefer placeholders such as `<SMTP_HOST>` and `<MAIL_FROM>`.
- Verify whether mail config comes from properties, database metadata, environment variables, or external secret management in the target instance.

## Relationships
- Email template -> business event or policy.
- Scheduler -> Dynamic REST API -> email behavior.
- Mail configuration -> environment-specific settings.
- Mail history -> audit/troubleshooting.
- Notification -> selection criteria -> user audience.
- Notification -> validity window -> UI display.

## Safe AI-agent usage
- Do not trigger email APIs during analysis.
- Redact recipient addresses and message contents if private.
- Verify whether templates include dynamic merge fields.
- Check scheduler side effects before enabling jobs.
- Do not enable a notification without validating the audience query.
- Do not publish real mail configuration values in docs or examples.

## TODO items to verify
- TODO: verify mail configuration keys.
- TODO: verify email scheduler behavior and retry handling.
- TODO: verify template merge-data behavior.
- TODO: verify SMTP/environment configuration source.
- TODO: verify notification rendering lifecycle and cleanup behavior.
- TODO: verify notification message type values and target platform values.

## Example
An interview reminder scheduler may send emails. Treat it as side-effecting until the API target and service logic are verified.
