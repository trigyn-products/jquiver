# JQuiver AI Agent Instructions

You are working inside a JQuiver-based project generated from a Maven archetype.

This project contains only a small AI bootstrap bundle. The complete JQuiver AI Knowledge Base is not bundled inside this project. It must be fetched from the configured KB source before performing detailed JQuiver development, metadata, database, UI, or troubleshooting tasks.

## Mandatory workflow

Before generating or modifying JQuiver code, configuration, pages, forms, Dynamic REST APIs, dashboards, templates, database queries, routing, security, scheduler behavior, file upload behavior, API clients, script libraries, notifications, business modules, or deployment configuration:

1. Read this file.
2. Read `.ai/kb-source.json`.
3. Check whether the full JQuiver KB exists locally at `.ai/kb/`.
4. If the KB is missing, run the appropriate fetch script.
5. Read `.ai/task-router.md`.
6. If the full KB exists, read `.ai/kb/AGENTS.md` and `.ai/kb/KB-MAP.md`.
7. Read the relevant full KB files, playbooks, and skills before generating output.
8. Use JQuiver-native conventions.
9. Do not generate generic Spring Boot, jQuery, Form.io, SQL, or database code when JQuiver-specific metadata patterns exist.

## Expected full KB location

```text
.ai/kb/
```

## Full KB source

See:

```text
.ai/kb-source.json
```

The configured source is intended to be a public GitHub repository. While `authRequired` is `false`, do not ask for a GitHub token. Fetch scripts download GitHub's branch zip archive automatically; maintainers should publish the full KB repository contents to GitHub, not distribute a separate hand-made zip.

AI tools may run the fetch script automatically only when shell and network access are available. If execution is blocked, tell the developer to run `.ai/scripts/fetch-jquiver-kb.ps1` on Windows or `.ai/scripts/fetch-jquiver-kb.sh` on Linux/macOS from the generated project root.

## JQuiver-specific areas to recognize

- Module router and menu metadata.
- Form Builder and Form.io/pluggable forms.
- Grid Utils listings.
- Dynamic REST APIs, including Java, FTL, Python, and server-side JavaScript/Nashorn-style logic where supported.
- Additional datasource metadata.
- File bins, including custom file bins.
- Autocomplete/typeahead, including single-select, multiselect, dependent, and datasource-backed lookups.
- Templates, email/XML, and webclient XML.
- Dashboards and dashlets.
- Scheduler jobs and scheduler logs.
- Multilingual resource bundles.
- Help manual assets.
- Notifications and mail flows.
- API clients.
- Script libraries.
- Business modules and tags.
- Users, roles, authentication, and permission management.
- Application configuration, environment, deployment, and troubleshooting.

## Safety rules

- Do not invent JQuiver internals.
- Prefer actual source code, database schema, exported metadata, and running instance behavior.
- Mark assumptions clearly with `TODO: Verify from actual JQuiver source code / database / instance export.`
- Never generate destructive SQL unless explicitly asked.
- Always recommend backup before metadata/database changes.
- Do not expose credentials, tokens, API client secrets, SMTP settings, uploaded files, resumes, candidate data, employee data, OTPs, or private mail content.
- Do not call side-effecting APIs, scheduler endpoints, email APIs, delete APIs, or save APIs during analysis unless explicitly asked.
- Do not read sensitive configuration files unless explicitly authorized.

## Fallback rule

If the full KB cannot be downloaded, continue only with clearly marked assumptions and ask the user to provide the KB source, source code, database export, or required JQuiver examples.
