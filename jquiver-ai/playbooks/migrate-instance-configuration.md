# Migrate Instance Configuration

## Goal
Move JQuiver metadata and instance configuration between environments safely.

## When to use
Use this playbook for environment promotion, cloning, comparison, or migration of a JQuiver instance.

## Required inputs
- Source environment.
- Target environment.
- JQuiver version for both environments.
- Metadata scope.
- Custom schemas.
- Upload folders.
- Datasource mapping.
- Approval and rollback plan.

## Files/tables/configuration to inspect first
- `jq_module_listing`.
- `jq_dynamic_form`.
- `jq_grid_details`.
- `jq_dynamic_rest_details`.
- `jq_additional_datasource`.
- `jq_file_upload_config`, `jq_file_upload`.
- `jq_dashboard`, `jq_dashlet`.
- `jq_resource_bundle`.
- Custom business schemas.
- Environment configuration.

## Step-by-step implementation approach
1. Inventory source metadata and custom schema dependencies.
2. Inventory target environment state.
3. Back up target platform and custom schemas.
4. Map datasource IDs and environment-specific connection values.
5. Migrate custom schema objects before metadata that depends on them.
6. Migrate metadata in dependency order.
7. Migrate upload folders if file metadata depends on them.
8. Update environment-specific placeholders.
9. Validate routes, forms, grids, APIs, dashboards, file bins, and schedulers.
10. Keep target schedulers disabled until reviewed if side effects are possible.

## Validation checklist
- Source and target versions compatible.
- Backups completed.
- Custom schemas restored.
- Datasource mappings correct.
- Routes resolve.
- Forms save.
- Grids load.
- APIs respond safely.
- Uploads/downloads work.
- Schedulers reviewed.

## Common mistakes
- Migrating metadata before custom schemas.
- Copying datasource credentials across environments.
- Forgetting upload folders.
- Keeping source environment URLs in target metadata.
- Enabling schedulers prematurely.

## Rollback plan
- Restore target database backup.
- Restore prior upload folder state.
- Revert environment configuration.
- Redeploy previous artifact if code changed.
- Disable migrated routes if partial rollback is needed.

## Related skills and reference docs
- `../knowledge-base/16-import-export-versioning.md`
- `../developer-runbook/database-backup-restore.md`
- `../developer-runbook/release-and-deployment-runbook.md`
- `../skills/jquiver-instance-analyzer/SKILL.md`

