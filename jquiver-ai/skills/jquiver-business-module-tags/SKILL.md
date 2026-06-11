---
name: jquiver-business-module-tags
description: Use for JQuiver business module and tag work involving jq_business_module, jq_business_module_entity_details, jq_tag_data, jq_tag_entity_details, grouped metadata, permissions JSON, and migration packaging.
---

# JQuiver Business Module Tags

## 1. Skill name
JQuiver Business Module Tags

## 2. Purpose
Guide an agent through grouping, tagging, reviewing, or migrating related JQuiver metadata entities.

## 3. Trigger phrases / when to use
- "business module"
- "tag creation"
- "tag metadata"
- "group module entities"
- "migration package"
- "business module export"

## 4. Required context
- Business module or tag name.
- Target metadata entities.
- Entity type/table for each target.
- Permission expectations.
- Migration/export goal.

## 5. Files to read first
- `../../knowledge-base/34-business-modules-and-tags.md`
- `../../knowledge-base/16-import-export-versioning.md`
- `../../playbooks/configure-business-module-and-tags.md`
- `../../reference/metadata-table-reference.md`

## 6. Step-by-step workflow
1. Confirm whether the task is grouping, tagging, reviewing, or migrating.
2. Inspect `jq_business_module` and `jq_business_module_entity_details`.
3. Inspect `jq_tag_data` and `jq_tag_entity_details` if tags are involved.
4. Validate each referenced entity in its owning metadata table.
5. Review permission JSON and role IDs.
6. Identify dependencies such as templates, APIs, grids, forms, and script libraries.
7. Preserve checksums and custom-update flags unless behavior is verified.
8. Prepare rollback rows before any metadata change.

## 7. Output format
Return:
- Business module/tag name.
- Linked entity list.
- Missing dependencies.
- Permission notes.
- Risks and TODOs.
- Rollback plan.

## 8. Safety rules
- Do not confuse business modules with route/menu modules.
- Do not copy permission JSON without verifying target role IDs.
- Recommend backup before association changes.
- Treat module JSON as sensitive metadata.

## 9. Examples
- HRS includes business modules such as Onboarding, MRM, Separation, and Candidate Success Accelerator.
- An MRM tag may group templates, grids, and forms.

## 10. Things not to do
- Do not tag only the visible route and miss the target entity.
- Do not migrate a business module without dependencies.
- Do not overwrite checksums blindly.
- Do not assume tags are only cosmetic until source confirms behavior.
