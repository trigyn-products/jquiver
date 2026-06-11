---
name: jquiver-application-configuration
description: Use for safe JQuiver application configuration analysis involving authentication mode, mail settings, scheduler base URL, upload location, security properties, environment placeholders, and secret redaction.
---

# JQuiver Application Configuration

## 1. Skill name
JQuiver Application Configuration

## 2. Purpose
Guide an agent through safe review and documentation of JQuiver environment and application configuration without exposing secrets.

## 3. Trigger phrases / when to use
- "application configuration"
- "environment configuration"
- "mail config"
- "upload location"
- "scheduler base URL"
- "authentication mode"
- "config properties"

## 4. Required context
- Target environment.
- Authorized configuration source.
- Configuration area to inspect.
- Whether sensitive config files may be read.
- Redaction requirements.

## 5. Files to read first
- `../../knowledge-base/35-application-configuration.md`
- `../../knowledge-base/26-deployment-and-environment.md`
- `../../developer-runbook/environment-configuration.md`
- `../../reference/environment-config-reference.md`
- `../../developer-runbook/safe-data-handling.md`

## 6. Step-by-step workflow
1. Confirm authorization before reading sensitive config files.
2. Identify config source and override order.
3. Redact passwords, tokens, private keys, SMTP secrets, and client secrets.
4. Map settings to runtime behavior: auth, mail, scheduler, upload path, security.
5. Compare config with metadata that depends on it.
6. Avoid claiming exact keys unless verified from source/config.
7. Record unresolved items as TODOs.
8. For changes, require backup and environment owner approval.

## 7. Output format
Return:
- Configuration area.
- Verified source.
- Redacted values or placeholders.
- Dependent metadata.
- Risks and TODOs.
- Validation steps.

## 8. Safety rules
- Do not read config files the user said to avoid.
- Do not print secrets.
- Do not treat dev config as production truth.
- Do not change config without approval.

## 9. Examples
- Scheduler DNS failures may require checking scheduler metadata and authorized base URL config.
- Mail issues may require SMTP placeholders and mail scheduler review.

## 10. Things not to do
- Do not copy real `application.yml` values into docs.
- Do not invent property keys.
- Do not enable production mail or schedulers during local testing.
- Do not assume configuration behavior without source/runtime verification.
