# Business Modules and Tags

## Purpose
Explain JQuiver business module grouping and tag metadata.

## When to use this file
Use this file when grouping related metadata entities, tagging forms/grids/templates/APIs, or migrating a functional package between instances.

## Related files
- `16-import-export-versioning.md`
- `23-jquiver-metadata-tables-reference.md`
- `30-ai-agent-working-rules.md`
- `../playbooks/configure-business-module-and-tags.md`
- `../reference/metadata-table-reference.md`

## Known facts
- Observed HRS metadata includes `jq_business_module` and `jq_business_module_entity_details`.
- Observed HRS business module names include Onboarding, Candidate Success Accelerator, MRM, and Separation.
- Observed HRS metadata includes `jq_tag_data` and `jq_tag_entity_details`.
- Tag entity details can include module/entity IDs, module JSON, checksum, and permissions metadata.

## Conceptual behavior
Business modules group related metadata entities so a functional area can be understood, reviewed, exported, or migrated as a package. Tags provide another way to label or associate metadata entities and may carry permission information.

## Relationships
- Business module -> business module entity details.
- Business module entity -> JQuiver metadata entity, such as template, route, form, grid, or Dynamic REST API.
- Tag -> tag entity details.
- Tag entity details -> module/entity metadata and permissions.

## Safe AI-agent usage
- Do not assume business modules are the same as menu modules.
- Before migration, list all entities attached to the business module or tag.
- Verify permission JSON before copying tag associations.
- Preserve checksums and custom-update flags unless source behavior says otherwise.
- Treat tag/module JSON as authoritative metadata snapshots until source confirms otherwise.

## TODO items to verify
- TODO: verify business module UI behavior and export behavior from source.
- TODO: verify tag permission JSON schema.
- TODO: verify whether tags affect runtime access or only organization/search.
- TODO: verify checksum behavior for tagged entities.

## Example
For an MRM migration, list `jq_business_module_entity_details` rows for the MRM business module and inspect each referenced metadata entity before exporting.
