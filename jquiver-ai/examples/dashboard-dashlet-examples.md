# Dashboard Dashlet Examples

## Status
Illustrative unless explicitly marked as observed from exported metadata.

## Purpose
Show realistic dashboard/dashlet patterns that avoid exposing private operational data.

## Example 1: Open Work Items

Goal:
- Show a count of active records by status.

Illustrative dashlet query:

```sql
-- Example only. Verify table names, status IDs, and permissions first.
SELECT status_name AS label, COUNT(*) AS value
FROM work_item_listing_view
WHERE is_active = 1
GROUP BY status_name
ORDER BY status_name;
```

Validation:
- Query is aggregate-only.
- Result is small.
- User role can view the underlying module.

## Example 2: Scheduled Interviews By Day

Goal:
- Show upcoming interview counts for the next 7 days.

Illustrative dashlet query:

```sql
-- Example only. Do not include candidate PII in dashboard aggregates.
SELECT DATE(scheduled_at) AS label, COUNT(*) AS value
FROM interview_schedule_view
WHERE scheduled_at >= CURRENT_DATE
  AND scheduled_at < DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY)
GROUP BY DATE(scheduled_at)
ORDER BY label;
```

Safety:
- Do not expose candidate names, emails, resumes, or phone numbers in summary widgets.

## Example 3: Metadata Health Dashboard

Goal:
- Show platform metadata counts.

Illustrative dashlet metrics:
- Forms count.
- Grids count.
- Dynamic REST API count.
- Scheduler count.
- Additional datasource count.

Observed export pattern:
- Dashboards use `jq_dashboard`.
- Dashlets use `jq_dashlet`.
- Association metadata links dashboards to dashlets.

TODO: Verify association table names and dashlet type lookup values in the target version.
