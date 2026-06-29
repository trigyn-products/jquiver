# JQuiver Bootstrap Workflow

Use this workflow whenever an AI agent is asked to work on this JQuiver project.

## Step 1: Identify the task

Classify the user request into one or more of these task types:

- Instance analysis
- CRUD module
- Module route or menu configuration
- Define home page
- Form Builder
- Form.io or external form embedding
- Grid Utils listing
- Dynamic REST API
- Secured REST API
- Additional datasource
- File upload or custom file bin
- Autocomplete/typeahead
- Template, email/XML, or webclient XML
- Dashboard or dashlet
- Scheduler
- Multilingual labels or resource bundles
- Help manual
- Notification or mail flow
- Script library
- API client
- Business module or tag
- User, role, permission, authentication, or RBAC
- Application/environment configuration
- Local setup, build/run, release, or deployment
- Production troubleshooting

## Step 2: Check full KB

Look for:

```text
.ai/kb/AGENTS.md
```

If it does not exist, download the KB using the configured fetch script.

For public distribution, the KB source is a public GitHub repository and no token is required. The fetch script downloads the repository's branch zip archive from GitHub; maintainers should push the full KB repository contents to GitHub, not maintain a separate zip file manually.

An AI coding agent may run the fetch script automatically only when it has shell and network access. Otherwise, ask the developer to run the Windows or Linux/macOS script once from the generated project root.

## Step 3: Read matching KB files

Use `.ai/task-router.md` for quick routing.

If the full KB exists, prefer:

```text
.ai/kb/KB-MAP.md
```

Then read the matching knowledge-base, reference, developer-runbook, playbook, and skill files.

## Step 4: Generate output

Use JQuiver-specific conventions only.

Avoid generic assumptions.

Where exact table names, APIs, code paths, lookup IDs, or configuration keys are not verified, add:

```text
TODO: Verify from actual JQuiver source code / database / instance export.
```

## Step 5: Validate output

Before finalizing generated code or configuration, check whether the output:

- Follows JQuiver routing and menu conventions.
- Uses metadata-driven JQuiver patterns.
- Avoids unnecessary custom code.
- Uses existing JQuiver utilities where available.
- Recommends backup before metadata/database changes.
- Avoids destructive SQL unless explicitly requested.
- Redacts credentials, PII, uploaded files, and secrets.
- Identifies side effects before APIs or schedulers are called.
- Clearly documents assumptions and TODOs.
