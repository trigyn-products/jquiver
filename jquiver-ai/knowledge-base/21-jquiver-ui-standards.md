# JQuiver UI Standards

## Purpose
Capture safe v1 UI standards for JQuiver metadata-driven pages.

## When to use this file
Use this file when creating or reviewing templates, forms, grids, dashboards, public pages, or embedded forms.

## Related files
- `08-templates.md`
- `04-form-builder.md`
- `06-grid-utils.md`
- `../reference/ui-standards.md`

## Known facts
- JQuiver pages may be assembled from routes, templates, dynamic forms, grids, JavaScript, CSS, and APIs.
- Known frontend context includes jQuery, pqGrid, and Form.io.
- Observed custom pages include public listing pages, OTP pages, forms, grids, dashboards, and embedded iframe examples.

## UI principles
- Keep navigation predictable.
- Use clear labels and consistent button behavior.
- Keep validation messages close to the relevant field.
- Use grids for scan-heavy listing pages.
- Use dashboards for summary metrics, not detailed transactional editing.
- Use resource bundles for reusable text where multilingual behavior is expected.
- Keep public pages minimal and focused.

## Metadata-driven UI concerns
- A visible label may come from a resource bundle or i18n row.
- A button may call a Dynamic REST API.
- A table may be a pqGrid backed by `jq_grid_details`.
- A form may mix HTML, JavaScript, metadata queries, and save logic.
- UI behavior may differ by role.

## Safe AI-agent usage
- Do not invent official CSS class names.
- Verify existing UI conventions from the target instance before proposing markup.
- Avoid adding hidden side effects to buttons.
- Check mobile/responsive behavior for public pages.

## TODO items to verify
- TODO: verify official UI component classes and layout conventions.
- TODO: verify validation message style.
- TODO: verify accessibility requirements.
- TODO: verify supported frontend library versions.

## Example
For a public job listing page, keep filters visible, grid results readable, and action buttons clear. Verify exact CSS and component patterns from the running instance or template metadata.

