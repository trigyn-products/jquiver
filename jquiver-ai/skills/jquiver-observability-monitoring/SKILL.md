---
name: jquiver-observability-monitoring
description: Use for JQuiver runtime observability, JavaMelody monitoring, monitoring-path checks, performance diagnostics, application.yml enablement review, pom.xml/dependency verification, and safe production monitoring guidance.
---

# JQuiver Observability Monitoring

## Purpose
Guide safe JavaMelody monitoring review for JQuiver Spring Boot applications.

## When to use
Use for JavaMelody, `/monitoring`, runtime monitoring, observability, performance diagnostics, slow requests, SQL timing, or production monitoring access-control questions.

## Files to read first
- `../../knowledge-base/26-deployment-and-environment.md`
- `../../knowledge-base/27-logging-audit-error-handling.md`
- `../../knowledge-base/29-performance-guidelines.md`
- `../../reference/environment-config-reference.md`
- `../../developer-runbook/environment-configuration.md`
- `../../developer-runbook/build-and-run-jquiver.md`

## JavaMelody enablement checklist
1. Inspect `application.yml` or `application.yaml`.
2. Check whether the `javamelody` block is commented, absent, or enabled.
3. Inspect `pom.xml` and dependency tree before changing dependencies.
4. Verify expected monitoring path and deployment context path.
5. Confirm access control before exposing the endpoint.
6. Test only in an approved environment.

## application.yml verification
- The archetype contains optional commented JavaMelody configuration.
- If the block is commented, document JavaMelody as available but disabled until uncommented/configured.
- Do not say JavaMelody is enabled by default.

## pom.xml/dependency verification
- Do not add or upgrade JavaMelody dependency versions blindly.
- If no active explicit JavaMelody dependency is present, mark TODO to verify whether it is provided through `java-web-starter` or another starter.
- Use dependency tree evidence before changing build files.

## Monitoring URL/path verification
- Default monitoring path is `/monitoring`.
- Actual path may use `javamelody.monitoring-path`.
- Context path, reverse proxy, and deployment settings may change the externally visible URL.
- Do not publish real production monitoring URLs.

## Security checklist
- Do not expose JavaMelody publicly without access control.
- For production, require authentication, IP restriction, VPN, reverse proxy protection, or equivalent controls.
- Treat SQL traces, request parameters, session details, stack traces, user data, production URLs, and credentials as sensitive.

## Safe troubleshooting workflow
1. Confirm the symptom and environment.
2. Verify JavaMelody configuration and dependency status.
3. Identify the expected monitoring path without exposing real URLs.
4. Review only sanitized observations.
5. Correlate findings with logs, datasource checks, and metadata consumers.
6. Record assumptions and TODOs.

## Output format
Return:
- Config status for `application.yml` or `application.yaml`.
- Whether JavaMelody config is commented or enabled.
- `pom.xml`/dependency status.
- Expected monitoring path.
- Security/access-control requirement.
- Verification steps.
- Assumptions/TODOs.

## Things not to do
- Do not treat JavaMelody as a JQuiver metadata table, Dynamic REST API, router page, or custom business module.
- Do not add raw logs, screenshots, SQL traces, request data, session/user data, stack traces, credentials, or production URLs to examples.
- Do not invent dependency versions.
- Do not enable or expose monitoring in production without explicit authorization and access control.
