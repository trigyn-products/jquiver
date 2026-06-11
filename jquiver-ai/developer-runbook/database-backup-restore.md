# Database Backup and Restore

## Purpose
Define safe backup and restore expectations before JQuiver metadata, schema, or data changes.

## Prerequisites
- Permission to perform backup/restore.
- Target database and schemas identified.
- Approved database tool available.
- Maintenance window confirmed for production restore operations.

## Inputs required
- Database engine: `<DB_ENGINE>`.
- Host placeholder: `<DB_HOST>`.
- Port placeholder: `<DB_PORT>`.
- Schemas: `<SCHEMA_LIST>`.
- Backup destination: `<BACKUP_LOCATION>`.
- Restore destination: `<RESTORE_TARGET>`.
- Change ticket or approval ID: `<APPROVAL_ID>`.

## Steps
1. Identify all schemas affected by the change.
2. Include platform metadata schema and any custom schemas.
3. Include upload folder backup if file metadata or storage paths are involved.
4. Use the approved backup tool for the database engine.
5. Store backup in an approved secure location.
6. Verify backup completion and file size.
7. Perform a restore test in a non-production environment when possible.
8. Record backup timestamp, schemas, tool, and operator.
9. Proceed with metadata or schema changes only after backup validation.
10. Keep rollback instructions attached to the change plan.

## Validation checklist
- All affected schemas included.
- Custom schemas included.
- Upload folder included if needed.
- Backup completed successfully.
- Restore test completed or risk accepted.
- Backup location secured.
- Backup metadata recorded.

## Common errors
- Backing up only the platform schema but not custom schema.
- Forgetting `jq_additional_datasource` dependencies.
- Forgetting file-upload folders.
- Not testing restore before production change.
- Storing backups in insecure local paths.

## Rollback/safety notes
- Restore should be tested before relying on it.
- Restoring production databases can overwrite data; require explicit approval.
- If only metadata changed, a targeted metadata rollback may be safer than full restore.
- Never run destructive restore commands without user approval.

## Related KB/reference/playbook files
- `../knowledge-base/16-import-export-versioning.md`
- `../knowledge-base/02-core-database-model.md`
- `safe-data-handling.md`
- `release-and-deployment-runbook.md`
- `../playbooks/migrate-instance-configuration.md`

