# Configure Script Library

## Goal
Create, attach, modify, or review a JQuiver script library safely.

## When to use
Use this playbook when reusable script logic is shared by Dynamic REST APIs, forms, templates, or other metadata entities.

## Required inputs
- Script library name.
- Script type.
- Target entity type and entity ID.
- Reason for creating or changing the library.
- Expected runtime variables and dependencies.
- Rollback owner/approver.

## Files/tables/configuration to inspect first
- `jq_script_lib_details`.
- `jq_script_lib_connect`.
- `jq_template_master` if script body is template-backed.
- Target metadata entity, such as `jq_dynamic_rest_details`.
- `knowledge-base/32-script-library.md`.
- `knowledge-base/07-dynamic-rest-api.md`.

## Step-by-step implementation approach
1. Find similar script libraries in the same instance.
2. Identify all existing connections for the target library.
3. Confirm script type and runtime scope.
4. Review the target entity that will load the library.
5. Back up current library and connection rows.
6. Make the smallest metadata change possible.
7. Test one target entity in a non-production environment.
8. Test every connected entity if modifying an existing shared library.
9. Review logs for runtime errors.

## Validation checklist
- Script library row exists.
- Connection row points to the intended entity.
- Script type is correct.
- Target API/page/form still loads.
- No unrelated connected entity regressed.
- Previous script content is recoverable.

## Common mistakes
- Editing a shared library without listing all consumers.
- Copying a script library without its template dependency.
- Assuming browser JavaScript behavior for server-side JavaScript.
- Forgetting custom-update/checksum conventions.

## Rollback plan
- Restore previous `jq_script_lib_details` row.
- Restore previous `jq_script_lib_connect` rows.
- Restore related template/script body if changed.
- Disable the consuming route/API if rollback is urgent and approved.

## Related skills and reference docs
- `../knowledge-base/32-script-library.md`
- `../knowledge-base/07-dynamic-rest-api.md`
- `../reference/metadata-table-reference.md`
- `../developer-runbook/database-backup-restore.md`
