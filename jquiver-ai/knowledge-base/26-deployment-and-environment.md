# Deployment and Environment

## Purpose
Explain deployment and environment concepts for JQuiver without assuming unverified configuration details.

## When to use this file
Use this file when preparing local, development, staging, or production deployment notes.

## Related files
- `16-import-export-versioning.md`
- `13-additional-datasource.md`
- `../developer-runbook/build-and-run-jquiver.md`
- `../developer-runbook/environment-configuration.md`
- `../reference/environment-config-reference.md`

## Known facts
- The analyzed folders include JAR files, SQL exports, `instanceId.txt`, file-upload folders, and environment files.
- Environment files were intentionally not read during analysis because the user stated they were not configured.
- Known backend technology context includes Java/Spring Boot and MariaDB.
- `application.yml` or `application.yaml` may configure `view.path` and `api.path`; defaults are `/view` for router pages and `/api` for REST APIs.

## Environment concerns
- Application runtime configuration.
- Database connection settings.
- Additional datasource records.
- File upload location.
- Mail settings.
- Authentication settings.
- Scheduler behavior.
- Context path/base URL.
- Router/API prefixes: `view.path` and `api.path`.
- Environment-specific secrets.

## Deployment concerns
- Code artifact version.
- Database schema version.
- Platform metadata version.
- Custom schema version.
- Upload folder migration.
- Runtime configuration.
- Smoke tests.
- Rollback plan.

## Safe AI-agent usage
- Do not assume an `application.yml` value is valid without user confirmation or source verification.
- Do not copy credentials from dumps into docs.
- Verify environment-specific datasource IDs and URLs.
- Recommend backup before deployment/migration.

## Form Builder URL rule
For Form Builder save/cancel/back URLs, grid action URLs, and router links, never hardcode `/cf/*` unless `view.path` or `api.path` is configured as `/cf` or an existing verified module proves it.

## TODO items to verify
- TODO: verify current build and run commands from source code.
- TODO: verify environment variable and configuration key names.
- TODO: verify deployment artifact naming and release process.
- TODO: verify profile behavior and default context path rules.

## Example
Moving an instance from development to staging may require JAR deployment, platform schema migration, custom schema migration, datasource retargeting, upload folder copy, and scheduler review.
