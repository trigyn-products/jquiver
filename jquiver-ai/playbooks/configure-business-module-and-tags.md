# Configure Business Module And Tags

## Goal
Group JQuiver metadata into a business module or tag related entities safely.

## When to use
Use this playbook when packaging a functional area, preparing migration, organizing metadata, or tagging related forms/grids/templates/APIs.

## Required inputs
- Business module or tag name.
- List of target metadata entities.
- Entity type/table for each target.
- Permission expectations.
- Migration/export requirement, if any.

## Files/tables/configuration to inspect first
- `jq_business_module`.
- `jq_business_module_entity_details`.
- `jq_tag_data`.
- `jq_tag_entity_details`.
- Target metadata tables for each entity.
- `knowledge-base/34-business-modules-and-tags.md`.

## Step-by-step implementation approach
1. List all entities that belong to the functional area.
2. Confirm entity IDs and metadata tables.
3. Check whether an existing business module/tag already represents the area.
4. Back up existing business module/tag rows.
5. Add or update module/tag associations in a non-production environment.
6. Verify permissions JSON if tag associations include permissions.
7. Test UI search/export/migration behavior if supported.
8. Review for missing dependent entities such as templates, APIs, grids, and file bins.

## Validation checklist
- Business module/tag exists.
- All intended entities are linked.
- No unintended entities are linked.
- Permission metadata is correct.
- Migration/export list includes dependencies.
- Checksums/custom flags are preserved where required.

## Common mistakes
- Confusing business modules with menu modules.
- Tagging only the route and missing target form/grid/API.
- Copying permission JSON without verifying role IDs.
- Missing templates or script libraries used by tagged entities.

## Rollback plan
- Restore previous module/tag association rows.
- Remove newly added associations if validation fails.
- Restore permission JSON from backup.

## Related skills and reference docs
- `../knowledge-base/34-business-modules-and-tags.md`
- `../knowledge-base/16-import-export-versioning.md`
- `../reference/metadata-table-reference.md`
- `../developer-runbook/database-backup-restore.md`
