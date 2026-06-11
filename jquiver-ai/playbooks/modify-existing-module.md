# Modify Existing Module

## Goal
Modify an existing JQuiver module safely with dependency awareness and rollback.

## When to use
Use this playbook when changing an existing route, form, grid, API, dashboard, file bin, datasource, permission, or template.

## Required inputs
- Existing module/route name.
- Requested change.
- Target environment.
- Current issue or enhancement goal.
- User role/security scope.
- Approval for metadata changes.

## Files/tables/configuration to inspect first
- `jq_module_listing`.
- Target metadata table, such as `jq_dynamic_form`, `jq_grid_details`, or `jq_dynamic_rest_details`.
- Related datasource and permission rows.
- Related templates, APIs, file bins, resource bundles, and schedulers.
- Existing similar metadata rows.

## Step-by-step implementation approach
1. Map the current module dependency chain.
2. Identify whether the module is system-owned or custom.
3. Export or copy current metadata values for rollback.
4. Confirm the exact requested behavior.
5. Make the smallest safe metadata/source change.
6. Test in non-production first.
7. Verify permissions and side effects.
8. Document before/after behavior and rollback steps.

Example read-only route trace:

```sql
-- Example only.
SELECT module_id, module_url, target_lookup_id, target_type_id
FROM jq_module_listing
WHERE module_url = '<EXISTING_ROUTE>';
```

## Validation checklist
- Dependency map completed.
- Backup captured.
- Change is scoped.
- Existing behavior not unintentionally broken.
- Permissions verified.
- Rollback tested or documented.

## Common mistakes
- Editing target metadata without checking route.
- Changing shared template used by multiple pages.
- Forgetting a scheduler or API uses the same metadata.
- Updating production first.

## Rollback plan
- Reapply previous metadata values.
- Restore previous template/form/API body.
- Revert business schema changes from backup if included.
- Disable route or feature flag only if verified safe.

## Related skills and reference docs
- `../knowledge-base/24-jquiver-page-lifecycle.md`
- `../developer-runbook/jquiver-metadata-navigation.md`
- `../reference/ai-agent-review-checklist.md`
- `../skills/jquiver-instance-analyzer/SKILL.md`

