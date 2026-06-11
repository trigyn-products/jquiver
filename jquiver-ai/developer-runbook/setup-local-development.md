# Setup Local Development

## Purpose
Prepare a local workstation for JQuiver development or instance analysis without assuming environment-specific secrets or paths.

## Prerequisites
- Access to the approved JQuiver source repository or packaged instance artifacts.
- Access to required database engine installers or local database service.
- Permission to read target SQL exports and upload folders.
- No production credentials stored in local documentation.

## Inputs required
- Source code path: `<JQUIVER_SOURCE_PATH>`.
- Local workspace path: `<LOCAL_WORKSPACE_PATH>`.
- Database engine and version: `<DB_ENGINE_VERSION>`.
- Java version: `<JAVA_VERSION>`.
- Build tool and version: `<BUILD_TOOL_VERSION>`.
- Local config template: `<LOCAL_CONFIG_TEMPLATE>`.
- Optional instance export path: `<INSTANCE_EXPORT_PATH>`.

## Steps
1. Confirm the required Java version from the JQuiver source repository.
2. Confirm build tool requirements from source files such as Maven or Gradle descriptors.
3. Install the verified Java and build tool versions.
4. Install or start a local database matching the target instance requirement.
5. Create local schemas using approved SQL exports or migrations.
6. Restore only non-production-safe seed data unless explicitly authorized.
7. Configure local environment values using placeholders first.
8. Keep secrets outside the repository.
9. Configure file-upload location to a local safe path such as `<LOCAL_UPLOAD_PATH>`.
10. Run build verification before starting the application.
11. Record local-only deviations in a private note, not in this KB.

## Validation checklist
- Java version verified.
- Build tool verified.
- Database service running.
- Local schemas created.
- Local configuration uses placeholders or local-only secrets.
- File-upload path exists and is writable.
- Application can be built or packaged.

## Common errors
- Reusing production credentials locally.
- Assuming packaged JAR configuration matches source configuration.
- Loading sensitive production data without approval.
- Forgetting upload folder permissions.
- Ignoring additional datasource configuration.

## Rollback/safety notes
- Keep local setup disposable.
- Use local-only schemas for testing.
- Do not commit local config files containing secrets.
- If setup scripts modify local databases, snapshot them first.

## Related KB/reference/playbook files
- `../knowledge-base/26-deployment-and-environment.md`
- `environment-configuration.md`
- `build-and-run-jquiver.md`
- `database-backup-restore.md`
- `safe-data-handling.md`

