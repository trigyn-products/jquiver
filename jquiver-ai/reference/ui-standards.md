# UI Standards

## Purpose
Capture UI standards for JQuiver screens.

## When to use this file
Use this file when reviewing forms, grids, dashboards, templates, and embedded pages.

## Related files
- `../knowledge-base/21-jquiver-ui-standards.md`

## Known facts
- Observed pages use Bootstrap-like layouts, grids, templates, and custom CSS.
- Observed public SBI page includes filters and a job listing grid.

## Form Builder standards
- Form Builder bodies should reuse verified JQuiver examples from the same instance or closest matching module.
- Form action buttons should use the standard action/button container. Do not left-align Save/Cancel actions unless a verified existing form does so.
- Required-field validation should match existing field highlight, focus, message container, and success/error display behavior.
- Do not invent custom Save/Cancel buttons, generic handlers, or generic Bootstrap/jQuery form structure unless explicitly asked.

## TODO items to verify
- TODO: Verify official CSS variables, layout classes, and component rules.
- TODO: Verify supported accessibility requirements.

## Example
Use clear button labels and validation messages for public forms.
