# Multilingual Resource Bundles

## Purpose
Explain JQuiver multilingual labels, route names, and resource bundle usage.

## When to use this file
Use this file when adding labels, UI text, messages, translations, route names, or locale-specific content.

## Related files
- `03-module-router.md`
- `21-jquiver-ui-standards.md`
- `../playbooks/configure-multilingual-labels.md`
- `../skills/jquiver-multilingual/SKILL.md`

## Known facts
- Observed resource metadata uses `jq_resource_bundle`.
- Observed route i18n metadata uses `jq_module_listing_i18n`.
- Exports include many resource bundle rows.

## Conceptual behavior
Resource bundles allow UI text to be managed separately from page implementation. Route labels may also be localized. This helps JQuiver screens support multiple languages and consistent UI messages.

## Relationships
- Resource key -> language ID -> translated text.
- Route -> localized module name.
- Template/form/grid UI -> resource keys.
- Validation/error messages -> resource bundle entries.

## Safe AI-agent usage
- Avoid hard-coding user-visible text when resource keys are expected.
- Verify language IDs before inserting rows.
- Check fallback behavior before removing or renaming keys.
- Keep resource keys stable where existing pages depend on them.

## TODO items to verify
- TODO: verify supported languages and language table structure.
- TODO: verify fallback behavior when translations are missing.
- TODO: verify import/export behavior for resource bundles.
- TODO: verify naming rules for resource keys.

## Example
When adding a new menu route, add or update route i18n metadata and any UI labels needed by the target page.

