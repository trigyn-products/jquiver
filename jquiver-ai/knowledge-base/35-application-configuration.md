# Application Configuration

## Purpose
Explain safe handling of JQuiver application-level configuration.

## When to use this file
Use this file when reviewing environment properties, authentication mode, mail settings, scheduler base URLs, upload locations, security settings, or instance-level feature flags.

## Related files
- `26-deployment-and-environment.md`
- `25-authentication-authorization-flow.md`
- `17-notifications-mail.md`
- `09-file-upload.md`
- `14-scheduler.md`
- `../developer-runbook/environment-configuration.md`
- `../reference/environment-config-reference.md`

## Known facts
- JQuiver deployments use Java/Spring Boot context.
- Previous analysis intentionally avoided unconfigured `application.yml` files unless explicitly authorized.
- Observed metadata includes authentication type, security type/properties, property master, scheduler configuration, mail-related behavior, and upload metadata.
- Exact property keys are environment/version-specific and must be verified from the target source/config.

## Configuration areas
Authentication:
- Database, LDAP, OAuth, SAML, or other configured authentication types may exist.
- Verify active mode and user/role sync behavior before changing anything.

Mail:
- SMTP/server details are environment-specific secrets.
- Use placeholders in docs and never copy passwords from config or exports.

File upload:
- Upload root/location and custom storage behavior must match file bin metadata.
- Verify path permissions and backup strategy.

Scheduler:
- Scheduler callbacks may depend on base URL, context path, API path, and network/DNS.
- Verify timezone and environment URL before enabling jobs.

Security:
- Security properties may include DDOS/security toggles and authentication-related settings.
- Verify source behavior before changing flags.

## Safe AI-agent usage
- Do not read or print sensitive config files unless the user explicitly asks.
- Redact secrets, tokens, passwords, client secrets, SMTP credentials, and private keys.
- Use environment placeholders for documentation.
- Recommend backup before configuration or metadata changes.

## TODO items to verify
- TODO: verify exact property keys from source/config after authorization.
- TODO: verify active authentication mode resolution.
- TODO: verify scheduler base URL/config property names.
- TODO: verify upload path and mail config keys.

## Example
If a scheduler fails with DNS or base URL errors, inspect scheduler metadata and authorized environment configuration, but redact URLs/secrets in any shared summary.
