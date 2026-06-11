# Debug Production Issue

## Goal
Diagnose a production JQuiver issue safely using read-first, low-risk steps.

## When to use
Use this playbook for broken pages, failed APIs, grid errors, scheduler failures, file upload issues, login/access issues, or bad production behavior.

## Required inputs
- Incident summary.
- Impacted environment.
- Impacted route/API/module.
- Error message or screenshot.
- Time window.
- Recent deployments/metadata changes.
- Approval level for diagnostics.

## Files/tables/configuration to inspect first
- Application logs.
- Browser/service HTTP response.
- Route metadata.
- Target form/grid/API/scheduler/file metadata.
- Datasource metadata.
- Recent deployment/change records.
- Security role metadata if access-related.

## Step-by-step implementation approach
1. Confirm impact and urgency.
2. Keep diagnostics read-only unless approved.
3. Identify the impacted route/API/module.
4. Map metadata dependencies.
5. Check recent changes.
6. Inspect logs with sensitive values redacted.
7. Run safe read-only SQL diagnostics.
8. Form a hypothesis.
9. Validate in lower environment if possible.
10. Propose smallest safe fix and rollback.
11. Apply only after approval.
12. Validate production and monitor.

## Validation checklist
- Symptom reproduced or confirmed.
- Impacted metadata identified.
- Logs reviewed.
- Data privacy preserved.
- Root cause or likely cause documented.
- Fix approved.
- Rollback ready.
- Post-fix validation completed.

## Common mistakes
- Editing production metadata before diagnosis.
- Running side-effecting APIs as tests.
- Ignoring recent datasource/config changes.
- Sharing logs with secrets or PII.
- Fixing UI symptom while API remains broken.

## Rollback plan
- Revert metadata to captured previous state.
- Restore database backup only with explicit approval.
- Redeploy previous artifact if code caused the issue.
- Disable affected route/scheduler only if approved and safer than leaving it active.

## Related skills and reference docs
- `../knowledge-base/27-logging-audit-error-handling.md`
- `../knowledge-base/30-ai-agent-working-rules.md`
- `../developer-runbook/common-sql-diagnostics.md`
- `../developer-runbook/safe-data-handling.md`

