# JQuiver Coding Standards

## Purpose
Capture safe v1 coding standards for JQuiver source, SQL, metadata scripts, templates, and generated documentation.

## When to use this file
Use this file before adding source code, Dynamic REST scripts, form scripts, SQL templates, metadata changes, or documentation examples.

## Related files
- `30-ai-agent-working-rules.md`
- `29-performance-guidelines.md`
- `../reference/coding-standards.md`
- `../reference/naming-conventions.md`

## Known facts
- Metadata exports contain SQL, JavaScript service logic, HTML, CSS, and template markup.
- Known backend technology context includes Java/Spring Boot.
- Known frontend context includes jQuery, pqGrid, and Form.io.

## General standards
- Prefer verified existing patterns in the target instance.
- Keep SQL parameterized where supported.
- Avoid embedding secrets in metadata, templates, or source.
- Keep custom JavaScript small and traceable.
- Name routes, forms, grids, APIs, and file bins consistently.
- Add comments only where logic is not obvious.
- Treat metadata scripts as production code.

## SQL standards
- Use read-only diagnostics first.
- Avoid destructive SQL unless explicitly requested.
- Include rollback guidance for writes.
- Check indexes/views for grid and dashboard queries.

## JavaScript/template standards
- Avoid global side effects unless required by existing JQuiver conventions.
- Verify API URLs and request payloads.
- Keep validation logic consistent with server-side persistence.

## Safe AI-agent usage
- Do not invent package names, utility methods, or helper APIs.
- Mark unknown conventions as TODO.
- Verify code paths from actual source before generating implementation guidance.

## TODO items to verify
- TODO: verify Java package conventions from source code.
- TODO: verify SQL formatter and migration practices.
- TODO: verify JavaScript service logic standards.
- TODO: verify official metadata naming standards.

## Example
When proposing a Dynamic REST DAO query, include datasource, parameters, expected result shape, side effects, and TODOs for unverified query type IDs.

