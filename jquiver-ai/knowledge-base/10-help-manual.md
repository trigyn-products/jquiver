# Help Manual

## Purpose
Explain the JQuiver help manual concept and its relationship to modules and file assets.

## When to use this file
Use this file when adding, analyzing, or troubleshooting in-app help/manual entries.

## Related files
- `09-file-upload.md`
- `12-multilingual-resource-bundles.md`
- `../playbooks/configure-help-manual.md`
- `../skills/jquiver-help-manual/SKILL.md`

## Known facts
- The analyzed exports include help manual metadata and help manual file uploads.
- Observed file bin `helpManual` stores manual-related assets.
- Help manual content may include images or PDFs.

## Conceptual behavior
The help manual module provides contextual guidance inside JQuiver. A help entry may be connected to modules, pages, or platform areas and may rely on uploaded media assets.

## Relationships
- Help entry -> target module/page.
- Help entry -> uploaded assets.
- Help content -> resource bundle labels, if multilingual support is used.
- Help visibility -> role/security behavior.

## Safe AI-agent usage
- Do not invent exact help manual table names until verified.
- Verify whether a help manual entry is system-owned or custom.
- Avoid deleting uploaded manual assets without explicit approval.
- Ensure screenshots or examples do not expose sensitive data.

## TODO items to verify
- TODO: verify exact help manual tables and relationships from source migrations.
- TODO: verify help manual route and rendering behavior.
- TODO: verify role/permission behavior for help entries.

## Example
When adding help for a grid, document the grid purpose, filters, row actions, and common errors, then attach assets through the verified help manual workflow.

