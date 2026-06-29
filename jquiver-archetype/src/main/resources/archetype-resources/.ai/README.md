# JQuiver AI Bootstrap Bundle

This folder contains the minimal AI guidance shipped with the JQuiver Maven archetype.

It helps AI coding agents recognize that this is a JQuiver project, avoid generic implementation assumptions, and fetch the complete JQuiver AI Knowledge Base before detailed work.

## This bundle includes

- Basic JQuiver project context.
- Full KB source location.
- Fetch and verification scripts.
- Task routing guide.
- Agent instructions.

## This bundle does not include

- Full JQuiver documentation.
- Complete skill packs.
- Complete examples.
- Fine-tuning data.
- Complete runbooks.
- Complete playbooks.

## First-time setup

The full KB is expected to live in a public GitHub repository. Do not upload a hand-made zip file for normal distribution. Publish the contents of the local `jquiver-ai-kb` repository to the configured GitHub repository and the fetch scripts will download GitHub's branch zip archive automatically.

No GitHub token is required while `authRequired` is `false` in `.ai/kb-source.json`.

Windows:

```powershell
.ai/scripts/fetch-jquiver-kb.ps1
```

Linux/macOS:

```bash
.ai/scripts/fetch-jquiver-kb.sh
```

The full KB will be downloaded to:

```text
.ai/kb/
```

AI coding agents do not download the KB merely because the `.ai` folder exists. They should read `.ai/AGENTS.md`; if they have shell and network access, they can run the fetch script automatically before JQuiver work. If shell or network access is blocked, the developer should run the matching script once from the generated project root.

## How to use with an AI coding agent

Tell the agent:

```text
This project was generated from the JQuiver Maven archetype.
First read .ai/AGENTS.md, then fetch the full JQuiver AI KB if it is not already available.
Use .ai/task-router.md for quick routing and .ai/kb/KB-MAP.md when the full KB is available.
Do not generate generic Spring Boot, jQuery, SQL, or Form.io code where JQuiver-specific metadata patterns exist.
Do not invent JQuiver internals. Mark assumptions as TODOs and recommend backup before metadata/database changes.
```

## Key JQuiver areas covered by the full KB

- Routes, menus, home page, and page lifecycle.
- Form Builder and Form.io.
- Grid Utils.
- Dynamic REST and secured APIs.
- Additional datasource.
- File bins and custom file bins.
- Autocomplete/typeahead.
- Templates, email/XML, and webclient XML.
- Scheduler.
- Dashboard and dashlet.
- Multilingual labels.
- Help manual.
- Notifications and mail.
- API clients.
- Script libraries.
- Business modules and tags.
- User management, authentication, roles, and permissions.
- Application configuration, deployment, troubleshooting, and safe data handling.
