# Build and Run JQuiver

## Purpose
Provide an operational flow for building and running JQuiver in a local or controlled non-production environment.

## Prerequisites
- Local development setup completed.
- Source code or packaged JAR available.
- Local database schemas available.
- Environment configuration prepared with non-production secrets.

## Inputs required
- Source path: `<JQUIVER_SOURCE_PATH>`.
- Runtime artifact path: `<JQUIVER_JAR_PATH>` if using a packaged JAR.
- Active profile: `<LOCAL_PROFILE>`.
- Context path: `<CONTEXT_PATH>`.
- Port: `<LOCAL_PORT>`.
- Database connection placeholders.
- File upload path: `<LOCAL_UPLOAD_PATH>`.

## Steps
1. Verify whether the run will use source build or packaged JAR.
2. If using source, run the source-verified build command.
3. If using a packaged JAR, verify the JAR version and target instance.
4. Confirm local database schemas are available.
5. Confirm local configuration values are set.
6. Confirm no production credentials are being used.
7. Start the application with the source-verified run command.
8. Watch startup logs for database, datasource, scheduler, and file-upload errors.
9. Open the local base URL: `http://localhost:<LOCAL_PORT>/<CONTEXT_PATH>/`.
10. Run smoke checks for route load, login/public page, grid load, file path access, and scheduler startup status.

## Validation checklist
- Build completes successfully.
- Application starts without fatal errors.
- Base URL responds.
- Database connection works.
- Additional datasource connections either work or are intentionally disabled for local use.
- File-upload location exists.
- No production scheduler side effects are enabled unintentionally.

## Common errors
- Wrong Java version.
- Wrong active profile.
- Missing schema or migration.
- Datasource points to an unavailable custom database.
- File upload path missing.
- Scheduler starts with production-like behavior in local environment.

## Rollback/safety notes
- Stop the local process before changing configuration.
- Disable or isolate side-effecting schedulers in local environments unless testing them intentionally.
- Restore local database snapshot if test metadata changes break startup.

## Related KB/reference/playbook files
- `setup-local-development.md`
- `environment-configuration.md`
- `../knowledge-base/26-deployment-and-environment.md`
- `../knowledge-base/14-scheduler.md`
- `../reference/environment-config-reference.md`

