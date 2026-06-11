# Autocomplete and Typeahead

## Purpose
Explain JQuiver autocomplete/typeahead metadata and safe usage.

## When to use this file
Use this file when configuring lookup suggestions, dynamic search inputs, typeahead fields, or metadata-backed autocompletes.

## Related files
- `13-additional-datasource.md`
- `04-form-builder.md`
- `08-templates.md`
- `../playbooks/configure-autocomplete-typeahead.md`
- `../skills/jquiver-autocomplete-typeahead/SKILL.md`

## Known facts
- Observed autocomplete metadata uses `jq_autocomplete_details`.
- Autocomplete metadata can reference a `datasource_id`.
- Observed HRS columns include `ac_id`, `ac_description`, `ac_select_query`, `ac_type_id`, and `datasource_id`.
- ARK custom datasource is used by autocompletes such as `ark_manufacturer`, `ark_distributors`, and `ark_reverse_processor`.
- SBI FAC includes autocompletes such as `intrv-candidates` and active user/panel lookups.
- HRS includes autocomplete examples for audit users, audit projects, interview candidates, interview panel levels/practices, RMS candidates, PMTool projects, and script libraries.

## Autocomplete variants
Single-select autocomplete:
- Use when the UI must select one entity and usually store one value.
- Validate the label field and stored value field separately.
- Confirm whether the selected row populates hidden fields.

Multiselect autocomplete:
- Use when the UI must associate many entities with one record.
- Confirm how selected values are serialized by the form/template before changing the query.
- Verify duplicate handling, max selection count, and display labels.
- TODO: verify exact multiselect widget conventions from source/template examples.

Dependent autocomplete:
- Use when suggestions depend on another field, such as state -> city, manufacturer -> product, or department -> user.
- Confirm parent field parameter names before writing the query.
- Verify behavior when the parent value is empty, changed, or cleared.
- Return only rows valid for the current parent selection.

Additional-datasource autocomplete:
- Use when lookup data lives outside the primary JQuiver schema.
- Confirm the referenced `datasource_id` exists and is available in the target environment.
- Verify query syntax for that database product, especially SQL Server/MySQL/MariaDB differences.

## Autocomplete lifecycle
1. A form/template field initializes an autocomplete by ID.
2. User types search text.
3. JQuiver executes the configured autocomplete query.
4. Query may use default schema or additional datasource.
5. Results return to the UI and are rendered as suggestions.
6. Selection may populate hidden fields, display labels, or trigger dependent logic.

## Relationships
- UI field -> autocomplete ID.
- Autocomplete metadata -> query.
- Query -> default schema or datasource ID.
- Autocomplete result -> form/template selection behavior.
- Autocomplete data -> permissions and privacy constraints.

## Safe AI-agent usage
- Verify request parameter names before writing queries.
- Limit results for performance.
- Avoid exposing PII in suggestions.
- Verify dependent fields and hidden values.
- Check custom datasource connectivity.
- For multiselect fields, verify serialization and save-query behavior.
- For dependent autocompletes, test parent-value changes and no-result behavior.

## TODO items to verify
- TODO: verify autocomplete request/response format from source code.
- TODO: verify pagination parameters and search parameter names.
- TODO: verify custom renderer conventions.
- TODO: verify security filtering for user/person lookups.
- TODO: verify `ac_type_id` meanings across JQuiver versions.
- TODO: verify exact multiselect and dependent autocomplete metadata conventions.

## Example
An `intrv-candidates` autocomplete should be reviewed carefully because candidate/applicant data may contain PII and should only return fields needed for selection.
