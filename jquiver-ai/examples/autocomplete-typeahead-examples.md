# Autocomplete Typeahead Examples

## Status
Illustrative unless explicitly marked as observed from exported metadata.

## Purpose
Provide generic examples for documenting JQuiver autocomplete and typeahead metadata.

## Example 1: Single-Select Lookup

Use case:
- Select one department from a department master.

Illustrative query:

```sql
-- Example only. Verify SQL dialect and parameters first.
SELECT department_id AS value, department_name AS label
FROM department_lookup
WHERE department_name LIKE CONCAT('%', :searchText, '%')
ORDER BY department_name
LIMIT :startIndex, :pageSize;
```

Expected UI behavior:
- User chooses one label.
- Form stores one value.
- Hidden fields are populated only if explicitly configured.

## Example 2: Multiselect Lookup

Use case:
- Select multiple approvers for a workflow.

Illustrative query:

```sql
-- Example only. Return minimal fields.
SELECT user_id AS value, full_name AS label
FROM active_user_view
WHERE full_name LIKE CONCAT('%', :searchText, '%')
ORDER BY full_name
LIMIT :startIndex, :pageSize;
```

Validation:
- Confirm selected-value serialization format.
- Confirm duplicate selections are blocked.
- Confirm the save query expects multiple values.

## Example 3: Dependent Autocomplete

Use case:
- Product suggestions depend on selected manufacturer.

Illustrative query:

```sql
-- Example only. Verify parent parameter name.
SELECT product_id AS value, product_name AS label
FROM product_lookup
WHERE manufacturer_id = :manufacturerId
  AND product_name LIKE CONCAT('%', :searchText, '%')
ORDER BY product_name
LIMIT :startIndex, :pageSize;
```

Validation:
- Parent value is required.
- Child value clears when parent changes.
- No results are shown for invalid parent values.

## Observed Export Pattern

Observed metadata:
- `jq_autocomplete_details.ac_id`
- `jq_autocomplete_details.ac_select_query`
- `jq_autocomplete_details.ac_type_id`
- `jq_autocomplete_details.datasource_id`

TODO: Verify `ac_type_id` meanings and response field requirements from the target JQuiver source/export.
