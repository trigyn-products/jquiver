# Configure Autocomplete Typeahead

## Goal
Configure or modify a JQuiver autocomplete/typeahead lookup safely.

## When to use
Use this playbook when a form, template, or UI field needs search-as-you-type suggestions from the default schema or an additional datasource.

## Required inputs
- Autocomplete ID/name: `<AUTOCOMPLETE_ID>`.
- Variant: single-select, multiselect, dependent, or additional-datasource autocomplete.
- Target field or UI page: `<TARGET_FIELD_OR_PAGE>`.
- Search input parameter name: `<SEARCH_PARAM>`.
- Parent field parameter names if dependent.
- Expected label/value fields.
- Target table/view/query.
- Datasource ID if using a custom schema.

## Files/tables/configuration to inspect first
- `jq_autocomplete_details`.
- `jq_additional_datasource` if `datasource_id` is used.
- Source form/template body that initializes the autocomplete.
- Backing table/view.
- `knowledge-base/31-autocomplete-typeahead.md`.

## Step-by-step implementation approach
1. Find a similar working autocomplete in the same instance.
2. Confirm the UI field expects autocomplete behavior.
3. Confirm whether it is single-select, multiselect, dependent, or additional-datasource driven.
4. Verify the backing query can return only safe, necessary fields.
5. Verify parent-field parameters if dependent.
6. Verify selected-value serialization if multiselect.
7. Verify the datasource ID if the query targets a custom schema.
8. Limit result size and filter by search text.
9. Add or update metadata only after backup approval.
10. Test with empty, partial, exact, and no-result searches.
11. Verify dependent field population if the selection drives hidden values.

Example read-only inspection SQL:

```sql
-- Example only. Verify schema/table names first.
SELECT ac_id, ac_description, datasource_id
FROM jq_autocomplete_details
WHERE ac_id = '<AUTOCOMPLETE_ID>';
```

## Validation checklist
- Autocomplete metadata exists.
- Query returns expected label/value fields.
- Result count is bounded.
- Datasource is correct.
- UI renders suggestions.
- Single/multiple selection behavior is correct.
- Dependent parent-field behavior is correct, if used.
- No PII is unnecessarily exposed.
- No console or server errors.

## Common mistakes
- Returning too many rows.
- Querying the wrong schema.
- Exposing private user/applicant data.
- Using unverified request parameter names.
- Forgetting dependent hidden fields.
- Returning duplicate values in multiselect suggestions.
- Not clearing child/dependent values when parent field changes.

## Rollback plan
- Keep previous `jq_autocomplete_details` row values before change.
- Revert to previous query/datasource ID if validation fails.
- Clear browser/application cache only if source confirms it is required.

## Related skills and reference docs
- `../skills/jquiver-autocomplete-typeahead/SKILL.md`
- `../knowledge-base/31-autocomplete-typeahead.md`
- `../knowledge-base/13-additional-datasource.md`
- `../reference/metadata-table-reference.md`
