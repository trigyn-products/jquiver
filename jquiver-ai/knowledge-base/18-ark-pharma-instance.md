# ARK Pharma Instance

## Purpose
Summarize verified ARK Pharma Returns instance knowledge and safe analysis guidance.

## When to use this file
Use this file when analyzing or documenting the ARK Pharma JQuiver instance or custom schema.

## Related files
- `13-additional-datasource.md`
- `20-external-form-embedding.md`
- `31-autocomplete-typeahead.md`
- `../examples/ark-instance-summary.md`

## Known facts
- Main schema: `ark_pharma_knowledge_base`.
- Custom schema: `ark_pharma_knowledge_base_custom`.
- Main domain tables include policies, exceptions, products, manufacturers, return instructions, hospitals, staff, employees, policy tests, and email templates.
- Custom tables include `Manufacturers`, `Processors`, `Wholesalers`, `LookupMaster`, `Lookup`, and `contact_us`.
- `jq_additional_datasource` includes `ARK CUSTOM`, which points to the custom schema.
- `ARK CUSTOM` is used by `ManufacturersGrid`, `Manufacturers-form`, and several autocompletes.
- External form embed examples include `single submit.html` and `multi submit.html`.

## Domain concepts
ARK Pharma Returns appears to model drug/product return policy behavior:
- Policy details define general return rules.
- Policy exceptions override or specialize policy behavior.
- Products can be linked to exceptions.
- Manufacturers, processors, and wholesalers provide external/custom reference data.
- Return instructions define operational return guidance.
- Email templates support policy-related communications.
- Hospital and staff tables support hospital-related examples or workflows.

## Safe AI-agent usage
- Treat manufacturer contact details, hospital contacts, and uploaded files as sensitive.
- Do not expose datasource credentials.
- Verify ARK .NET/external integration before describing it as production behavior.
- Prefer schema-level summaries over raw row dumps.

## TODO items to verify
- TODO: verify ARK runtime routes against a running instance.
- TODO: verify ARK business rules from source code or product owners.
- TODO: verify external ARK .NET integration behavior.
- TODO: verify exact policy evaluation logic.

## Example
To understand the ARK policy listing page, inspect route metadata, `ark-policy-detailsGrid`, `ark_policy_listing_view`, policy/exception tables, and any filters or permissions.

