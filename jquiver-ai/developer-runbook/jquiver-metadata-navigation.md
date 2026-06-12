# JQuiver Metadata Navigation

## Purpose
Provide a repeatable path for tracing JQuiver runtime behavior through metadata.

## Prerequisites
- Target route, page, form, grid, API, dashboard, scheduler, or symptom identified.
- Read access to metadata tables.
- Basic understanding of JQuiver page lifecycle.

## Inputs required
- Route URL or module alias: `<ROUTE_URL>`.
- Target metadata name or ID: `<TARGET_ID>`.
- Platform schema: `<JQUIVER_SCHEMA>`.
- Custom schema list: `<CUSTOM_SCHEMA_LIST>`.
- User role or security context, if access-related.

## Steps
1. Start from route metadata when a page URL is known.
2. Identify target type and target ID.
3. Follow target ID to form, grid, template, dashboard, API, or URL metadata.
4. Inspect localized labels if UI text is relevant.
5. Inspect role/entity permissions if access is relevant.
6. Inspect datasource ID if the target queries data.
7. Inspect backing table or view.
8. Inspect related Dynamic REST APIs referenced by templates/forms.
9. Inspect file bins if upload/download is present.
10. Inspect schedulers if behavior is time-based.
11. For Form Builder pages, locate a verified form example before proposing HTML changes; record its button layout, validation/message pattern, save behavior, and navigation URLs.
12. Draw a dependency chain before proposing a change.

## Validation checklist
- Route found.
- Target type identified.
- Target metadata found.
- Datasource dependency identified.
- Backing table/view exists.
- Security dependencies checked.
- Related API/file/scheduler dependencies checked.
- Form Builder HTML changes are based on a verified existing form, not generic Bootstrap/jQuery.

## Common errors
- Editing a form without checking route permissions.
- Editing a grid without checking datasource.
- Fixing a template while ignoring the API it calls.
- Assuming labels are hard-coded when resource bundles are used.

## Rollback/safety notes
- Navigation is read-only.
- Any metadata update discovered from this process requires backup and playbook review.
- Keep dependency chain for rollback planning.

## Related KB/reference/playbook files
- `../knowledge-base/24-jquiver-page-lifecycle.md`
- `../knowledge-base/23-jquiver-metadata-tables-reference.md`
- `../reference/metadata-table-reference.md`
- `../playbooks/modify-existing-module.md`
- `common-sql-diagnostics.md`
