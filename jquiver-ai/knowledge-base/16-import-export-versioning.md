# Import, Export, and Versioning

## Purpose
Explain safe concepts for JQuiver metadata movement, instance migration, and version-aware changes.

## When to use this file
Use this file when migrating modules, comparing instances, exporting metadata, importing metadata, or planning upgrades.

## Related files
- `02-core-database-model.md`
- `13-additional-datasource.md`
- `../playbooks/migrate-instance-configuration.md`
- `../developer-runbook/database-backup-restore.md`
- `../developer-runbook/release-and-deployment-runbook.md`

## Known facts
- Analyzed SQL dumps include platform metadata and instance business data.
- Several metadata tables include checksums and custom-update flags.
- JQuiver instance behavior can depend on platform metadata, custom schemas, upload folders, and datasource configuration together.

## Migration concepts
A complete instance migration may need:
- Platform metadata.
- Custom business schema.
- Views and lookup data.
- Additional datasource records.
- File upload metadata and physical upload folder contents.
- Resource bundles.
- Roles and permissions.
- Scheduler definitions.
- Environment-specific configuration.

## Safe AI-agent usage
- Never migrate credentials blindly between environments.
- Verify datasource IDs and target database names.
- Recommend backup before import/export or metadata restore.
- Compare source and target JQuiver versions.
- Avoid overwriting system metadata without explicit approval.

## TODO items to verify
- TODO: verify official import/export tables, stored procedures, and UI flows.
- TODO: verify revision/version tables and checksum semantics.
- TODO: verify compatibility rules across JQuiver versions.
- TODO: verify module packaging/export format.

## Example
Migrating ARK `ManufacturersGrid` requires not only grid metadata, but also the `ARK CUSTOM` datasource and the custom `Manufacturers` table in the target environment.

