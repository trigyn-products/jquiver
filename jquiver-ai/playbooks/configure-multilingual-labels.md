# Configure Multilingual Labels

## Goal
Add or update JQuiver resource bundle labels and localized route names.

## When to use
Use this playbook when UI text, route labels, validation messages, or module labels need multilingual support.

## Required inputs
- Resource key or route ID.
- Target languages.
- Text values.
- Fallback/default language.
- Target page/module.

## Files/tables/configuration to inspect first
- `jq_resource_bundle`.
- `jq_module_listing_i18n`.
- Language metadata table. TODO: verify from database/source.
- Existing resource keys for naming pattern.
- Target template/form/grid using the label.

## Step-by-step implementation approach
1. Find similar resource keys.
2. Verify language IDs.
3. Decide stable resource key names.
4. Add or update resource rows after backup.
5. Add route i18n rows if route/menu label changes.
6. Test default language.
7. Test each target language.
8. Verify fallback behavior.

Example read-only key check:

```sql
-- Example only.
SELECT resource_key, language_id
FROM jq_resource_bundle
WHERE resource_key = '<RESOURCE_KEY>';
```

## Validation checklist
- Resource key exists for required languages.
- Route label renders.
- Fallback works.
- No duplicate/conflicting keys.
- Text fits UI.
- Encoding displays correctly.

## Common mistakes
- Wrong language ID.
- Duplicate resource key with inconsistent meaning.
- Hard-coded text remains in template.
- Text too long for UI.
- Missing fallback language.

## Rollback plan
- Restore previous resource bundle rows.
- Restore previous route i18n rows.
- Revert template references if changed.

## Related skills and reference docs
- `../skills/jquiver-multilingual/SKILL.md`
- `../knowledge-base/12-multilingual-resource-bundles.md`
- `../reference/naming-conventions.md`
- `../reference/ui-standards.md`

