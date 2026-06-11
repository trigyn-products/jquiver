---
name: jquiver-notification-mail
description: Use for JQuiver notification and mail analysis/configuration involving jq_generic_user_notification, email/XML templates, scheduler-triggered mail APIs, audience criteria, mail configuration placeholders, and mail side effects.
---

# JQuiver Notification Mail

## 1. Skill name
JQuiver Notification Mail

## 2. Purpose
Guide an agent through safe configuration or troubleshooting of JQuiver in-app notifications, mail templates, and scheduler/API-driven email flows.

## 3. Trigger phrases / when to use
- "notification"
- "mail configuration"
- "email template"
- "email/xml"
- "reminder mail"
- "failed mail"
- "scheduler email"

## 4. Required context
- Notification or email use case.
- Audience/recipient criteria.
- Template or message content.
- Triggering API/scheduler, if any.
- Test recipient plan.
- Environment mail configuration owner.

## 5. Files to read first
- `../../knowledge-base/17-notifications-mail.md`
- `../../knowledge-base/08-templates.md`
- `../../knowledge-base/14-scheduler.md`
- `../../playbooks/configure-notification-mail.md`
- `../../developer-runbook/troubleshoot-scheduler.md`

## 6. Step-by-step workflow
1. Decide whether the task is notification, email, or both.
2. Inspect `jq_generic_user_notification` for in-app messages.
3. Inspect template and Dynamic REST metadata for email flows.
4. Inspect scheduler metadata if mail is scheduled.
5. Validate audience criteria with read-only checks.
6. Render templates with safe sample data.
7. Test only with approved test recipients.
8. Review failed mail/scheduler logs.
9. Keep production enablement disabled until approved.

## 7. Output format
Return:
- Notification/mail flow map.
- Audience criteria.
- Template/API/scheduler chain.
- Side effects.
- Risks/TODOs.
- Test and rollback plan.

## 8. Safety rules
- Do not trigger production emails during analysis.
- Redact recipient addresses and private message bodies.
- Use placeholders for SMTP and mail credentials.
- Verify audience before enabling notifications.

## 9. Examples
- HRS includes interview reminder, exit interview, audit, L&D, and IJP mail scheduler patterns.
- ARK has policy email template mappings.

## 10. Things not to do
- Do not enable a scheduler before testing its API target.
- Do not use broad audience criteria casually.
- Do not publish real mail configuration.
- Do not expose private email content.
