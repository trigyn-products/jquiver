---
name: jquiver-autocomplete-typeahead
description: Use for JQuiver autocomplete/typeahead analysis and configuration involving jq_autocomplete_details, single-select, multiselect, dependent autocomplete, datasource-linked lookups, and query safety.
---

# JQuiver Autocomplete Typeahead

## 1. Skill name
JQuiver Autocomplete Typeahead

## 2. Purpose
Guide an agent through safe configuration or troubleshooting of JQuiver autocomplete fields and typeahead lookup metadata.

## 3. Trigger phrases / when to use
- "autocomplete"
- "typeahead"
- "single select lookup"
- "multiselect autocomplete"
- "dependent autocomplete"
- "lookup suggestions"
- "ac_id"

## 4. Required context
- Autocomplete ID.
- Target form/template field.
- Variant: single-select, multiselect, dependent, or datasource-backed.
- Search parameter name.
- Expected label/value fields.
- Parent field parameters, if dependent.

## 5. Files to read first
- `../../knowledge-base/31-autocomplete-typeahead.md`
- `../../knowledge-base/13-additional-datasource.md`
- `../../playbooks/configure-autocomplete-typeahead.md`
- `../../examples/autocomplete-typeahead-examples.md`
- `../../reference/metadata-table-reference.md`

## 6. Step-by-step workflow
1. Inspect `jq_autocomplete_details` for the target `ac_id`.
2. Find a similar working autocomplete in the same instance.
3. Identify variant and UI serialization behavior.
4. Verify query parameters such as search text, pagination, and parent field values.
5. Verify datasource ID if present.
6. Ensure result count is bounded.
7. Ensure returned fields expose only necessary data.
8. Test empty, partial, exact, no-result, and special-character searches.
9. Test dependent clearing and multiselect duplicate behavior when applicable.

## 7. Output format
Return:
- Autocomplete ID and variant.
- Query/datasource summary.
- Expected result shape.
- UI field dependencies.
- Risks and TODOs.
- Test checklist.

## 8. Safety rules
- Do not expose PII in suggestions.
- Limit results for performance.
- Use parameterized query patterns where supported.
- Verify parent parameter names before changing dependent queries.

## 9. Examples
- ARK autocompletes include manufacturer and distributor lookups from a custom datasource.
- HRS includes interview candidate and panel autocompletes.

## 10. Things not to do
- Do not return full user/applicant records when only label/value is needed.
- Do not assume multiselect stores values the same way as single-select.
- Do not copy a datasource ID across instances.
- Do not leave child values populated after parent changes.
