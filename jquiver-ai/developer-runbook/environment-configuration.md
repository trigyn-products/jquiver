# Environment Configuration

## Purpose
Guide safe review and preparation of JQuiver environment configuration.

## Prerequisites
- Target environment identified.
- Access to approved configuration source.
- Permission to inspect configuration.
- Secrets handling policy understood.

## Inputs required
- Environment name: `<ENVIRONMENT_NAME>`.
- Context path: `<CONTEXT_PATH>`.
- Application port: `<APP_PORT>`.
- Default datasource placeholders.
- Additional datasource placeholders.
- File-upload path: `<FILE_UPLOAD_PATH>`.
- Mail configuration placeholders.
- Authentication configuration placeholders.
- Scheduler policy for the environment.

## Steps
1. Identify configuration sources used by the target deployment.
2. Confirm which configuration files are valid for the environment.
3. Do not rely on unconfigured or sample config files.
4. Redact secrets before sharing configuration snippets.
5. Verify default database configuration.
6. Verify additional datasource metadata and environment-specific target databases.
7. Verify file-upload location.
8. Verify base URL, context path, and public route assumptions.
9. Verify mail settings without sending test mail unless approved.
10. Verify scheduler behavior for the environment.
11. Record unresolved configuration keys as TODOs.

## Validation checklist
- Active configuration source confirmed.
- No secrets copied into documentation.
- Database targets confirmed.
- File-upload path confirmed.
- Public base URL confirmed.
- Scheduler policy confirmed.
- Authentication mode confirmed or marked TODO.

## Common errors
- Reading stale or unconfigured `application.yml`.
- Copying datasource passwords into notes.
- Forgetting environment-specific additional datasource records.
- Leaving production mail or scheduler behavior enabled in local tests.

## Rollback/safety notes
- Keep previous known-good configuration available.
- Changes to configuration should be reversible.
- For production, use the approved deployment process and change window.

## Related KB/reference/playbook files
- `../knowledge-base/26-deployment-and-environment.md`
- `../reference/environment-config-reference.md`
- `troubleshoot-datasource.md`
- `troubleshoot-scheduler.md`
- `safe-data-handling.md`

