# Grid Examples

## Status
Illustrative unless explicitly marked as observed from exported metadata.

## Purpose
Show generic Grid Utils examples that are safe for AI-agent guidance.

## Example 1: Basic Listing Grid

Use case:
- List active records with search, sort, and edit action.

Illustrative metadata:
- `grid_id`: `sample-record-grid`
- `grid_name`: `Sample Record Grid`
- `grid_table_name`: `sample_record_listing_view`
- `query_type`: TODO: verify lookup value from target version.
- `datasource_id`: null for default schema, or `<DATASOURCE_ID>` for external schema.

Illustrative SQL backing view:

```sql
-- Example only. Verify table names and permissions first.
SELECT
  record_id,
  record_number,
  record_title,
  status_name,
  updated_date
FROM sample_record_listing_view
WHERE is_deleted = 0;
```

Validation:
- Pagination works.
- Sorting works.
- Search/filter criteria are bounded.
- Edit action opens a route the user can access.

## Example 2: Read-Only Reporting Grid

Use case:
- Show aggregate monthly counts.

Illustrative query:

```sql
-- Example only.
SELECT report_month, status_name, total_count
FROM monthly_status_summary_view
ORDER BY report_month DESC, status_name;
```

Safety:
- Prefer aggregate views for reporting.
- Avoid row-level private data unless roles require it.

## Observed Export Pattern

Observed metadata:
- Grid definitions use `jq_grid_details`.
- Grids may reference default schema or additional datasource via `datasource_id`.
- Grid pages usually depend on route metadata and role/entity permissions.

TODO: Verify exact grid type, query type, filter format, and action conventions from the target export/source.
