# Release and Deployment Runbook

## Purpose
Provide an operational deployment checklist for JQuiver releases and instance configuration promotion.

## Prerequisites
- Approved release artifact.
- Target environment confirmed.
- Backup plan approved.
- Rollback plan approved.
- Deployment window confirmed.
- Stakeholders notified.

## Inputs required
- Release version: `<RELEASE_VERSION>`.
- Artifact path: `<ARTIFACT_PATH>`.
- Target environment: `<TARGET_ENVIRONMENT>`.
- Platform schema: `<JQUIVER_SCHEMA>`.
- Custom schemas: `<CUSTOM_SCHEMA_LIST>`.
- Upload folder path: `<UPLOAD_FOLDER_PATH>`.
- Config source: `<CONFIG_SOURCE>`.
- Change approval: `<APPROVAL_ID>`.

## Steps
1. Confirm release scope: code, metadata, schema, configuration, upload files, or all.
2. Review changelog and migration notes.
3. Back up affected platform and custom schemas.
4. Back up upload folders if file metadata/storage changes are included.
5. Verify target environment configuration.
6. Deploy artifact using approved process.
7. Apply database migrations or metadata imports using approved process.
8. Validate additional datasource connectivity.
9. Validate file upload path.
10. Validate authentication and role access.
11. Validate key routes, forms, grids, APIs, dashboards, and schedulers.
12. Monitor logs after deployment.
13. Record deployment result and unresolved TODOs.

## Validation checklist
- Artifact version confirmed.
- Database backup completed.
- Custom schema backup completed.
- Config verified.
- Application starts.
- Smoke test routes pass.
- Dynamic REST smoke tests pass without side effects.
- Scheduler policy confirmed.
- Rollback path ready.

## Common errors
- Deploying code without required metadata.
- Migrating metadata without custom schema changes.
- Forgetting upload folders.
- Leaving environment-specific datasource values from another environment.
- Running production scheduler jobs unexpectedly.

## Rollback/safety notes
- Rollback may require artifact rollback, database restore, metadata rollback, config rollback, and upload folder restore.
- Use targeted rollback where possible.
- Do not perform production restore without explicit approval.
- Keep old artifact available until post-deploy validation passes.

## Related KB/reference/playbook files
- `database-backup-restore.md`
- `environment-configuration.md`
- `../knowledge-base/16-import-export-versioning.md`
- `../knowledge-base/26-deployment-and-environment.md`
- `../playbooks/migrate-instance-configuration.md`

