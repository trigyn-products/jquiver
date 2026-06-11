---
name: jquiver-script-library
description: Use for JQuiver script library analysis and configuration involving jq_script_lib_details, jq_script_lib_connect, reusable server-side script dependencies, entity attachments, runtime scope, and shared-library impact review.
---

# JQuiver Script Library

## 1. Skill name
JQuiver Script Library

## 2. Purpose
Guide an agent through safe review or modification of reusable JQuiver script libraries.

## 3. Trigger phrases / when to use
- "script library"
- "script_lib"
- "shared script"
- "attach script"
- "library connection"
- "reusable REST logic"

## 4. Required context
- Script library ID/name.
- Script type.
- Target entity ID.
- Consumer list, if known.
- Runtime dependency and expected behavior.

## 5. Files to read first
- `../../knowledge-base/32-script-library.md`
- `../../playbooks/configure-script-library.md`
- `../../knowledge-base/07-dynamic-rest-api.md`
- `../../knowledge-base/08-templates.md`
- `../../reference/metadata-table-reference.md`

## 6. Step-by-step workflow
1. Inspect `jq_script_lib_details`.
2. Inspect `jq_script_lib_connect`.
3. List all connected entities before editing.
4. Verify template/script body dependency.
5. Verify runtime scope and script type.
6. Test every consumer in non-production.
7. Preserve previous script body and connection rows.
8. Document unresolved runtime details as TODOs.

## 7. Output format
Return:
- Script library map.
- Connection/consumer list.
- Runtime assumptions.
- Impact/risk level.
- TODOs and rollback notes.

## 8. Safety rules
- Treat script libraries as shared executable code.
- Do not edit until all consumers are known.
- Recommend backup before changes.
- Redact secrets in script bodies.

## 9. Examples
- HRS Dynamic REST editor metadata references script library connections.
- A script library can be attached to multiple REST APIs.

## 10. Things not to do
- Do not assume one library has one consumer.
- Do not copy a library without its template dependency.
- Do not treat server-side JavaScript as browser JavaScript.
- Do not remove a connection without testing the target entity.
