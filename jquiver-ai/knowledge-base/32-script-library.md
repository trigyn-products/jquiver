# Script Library

## Purpose
Explain JQuiver script library metadata and how reusable scripts should be analyzed safely.

## When to use this file
Use this file when a Dynamic REST API, form, template, or other metadata entity references shared server-side script logic.

## Related files
- `07-dynamic-rest-api.md`
- `08-templates.md`
- `30-ai-agent-working-rules.md`
- `../playbooks/configure-script-library.md`
- `../reference/metadata-table-reference.md`

## Known facts
- Observed HRS metadata includes `jq_script_lib_details` and `jq_script_lib_connect`.
- Observed `jq_script_lib_details` fields include `script_lib_id`, `template_id`, `library_name`, `description`, `script_type`, and custom update metadata.
- Observed `jq_script_lib_connect` links a script library to a module/entity through `module_type_id` and `entity_id`.
- HRS Dynamic REST editor metadata referenced script library connections for REST APIs.

## Conceptual behavior
A script library is reusable executable logic that can be attached to one or more metadata entities. It should be treated as shared code: a change may affect every connected API/page/form.

## Relationships
- Script library -> template or script body.
- Script library -> connection rows.
- Connection row -> module type/entity ID.
- Dynamic REST/form/template -> script library dependency.

## Safe AI-agent usage
- Do not edit a script library before listing all connected entities.
- Treat script libraries as server-side executable behavior.
- Verify script type values before creating or migrating script libraries.
- Do not copy a library between instances until all dependent templates, APIs, and permissions are known.
- Keep previous script body/checksum before any change.

## TODO items to verify
- TODO: verify script type lookup values from source/database.
- TODO: verify how `template_id` stores or resolves the executable script body.
- TODO: verify script loading order when multiple libraries are attached.
- TODO: verify runtime scope available to scripts.

## Example
If a Dynamic REST API has script-library connections, inspect `jq_script_lib_connect` by entity ID before editing the API service logic.
