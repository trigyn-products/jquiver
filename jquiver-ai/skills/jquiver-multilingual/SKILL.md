---
name: jquiver-multilingual
description: Use for JQuiver multilingual label and resource bundle work involving jq_resource_bundle, jq_module_listing_i18n, language IDs, fallback behavior, UI text, route labels, and translated metadata.
---

# JQuiver Multilingual

## 1. Skill name
JQuiver Multilingual

## 2. Purpose
Guide an agent through safe configuration or troubleshooting of JQuiver multilingual labels and resource bundles.

## 3. Trigger phrases / when to use
- "multilingual"
- "resource bundle"
- "translate labels"
- "module label"
- "language ID"
- "i18n"
- "localized text"

## 4. Required context
- Target language(s).
- Resource keys or route/module IDs.
- Existing text and desired translation.
- Fallback expectations.
- UI page where labels appear.

## 5. Files to read first
- `../../knowledge-base/12-multilingual-resource-bundles.md`
- `../../playbooks/configure-multilingual-labels.md`
- `../../reference/naming-conventions.md`
- `../../reference/ui-standards.md`
- `../../reference/metadata-table-reference.md`

## 6. Step-by-step workflow
1. Inspect `jq_resource_bundle` for text keys.
2. Inspect `jq_module_listing_i18n` for route/menu labels.
3. Verify language IDs in the target instance.
4. Find existing key naming patterns.
5. Add or update labels only after backup approval.
6. Test page rendering in each target language.
7. Verify fallback behavior for missing keys.
8. Check text length and UI fit.

## 7. Output format
Return:
- Resource keys/module labels.
- Language IDs.
- Proposed text.
- Fallback notes.
- Risks/TODOs.
- Validation checklist.

## 8. Safety rules
- Do not overwrite existing translations without preserving prior values.
- Do not invent language IDs.
- Verify UI fit for long translations.
- Recommend backup before metadata writes.

## 9. Examples
- Route names use `jq_module_listing_i18n`.
- General UI labels may use `jq_resource_bundle`.

## 10. Things not to do
- Do not hard-code text in templates when a resource key should be used.
- Do not assume English fallback behavior without testing.
- Do not mix languages in one key.
- Do not translate technical IDs.
