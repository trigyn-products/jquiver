# Environment Configuration Reference

## Purpose
Document environment and configuration keys for JQuiver.

## When to use this file
Use this file when preparing local, dev, staging, or production configuration.

## Related files
- `../knowledge-base/26-deployment-and-environment.md`
- `../developer-runbook/environment-configuration.md`

## Known facts
- Environment files were present in analyzed instance folders but were not read because they were not configured.
- Database metadata includes properties in `jq_property_master`.
- `application.yml` or `application.yaml` can configure router/API prefixes with `view.path` and `api.path`.
- If not configured, router/page links default to `/view/{router-path}` and REST links default to `/api/{api-path}`.
- Do not invent `/cf/*` unless `view.path: /cf` is configured or the prefix is verified from an existing working module.
- Form Builder save URLs, cancel/back URLs, grid action URLs, and router links must use configured `view.path`/`api.path` values. Defaults are `/view` and `/api`.
- Verified auth/system endpoints from `Jquiver Database Authentication.postman_collection.json` may use `/japi` and `/cf`, such as `/japi/login` and `/cf/captcha/*`; do not treat these as evidence that router `view.path` or Dynamic REST `api.path` is `/cf`.
- The verified archetype `application.yml` includes optional commented JavaMelody config. If `javamelody` is commented, treat monitoring as available but disabled until uncommented/configured.
- JavaMelody default path is `/monitoring`, configurable with `javamelody.monitoring-path`; context path, reverse proxy, and deployment settings may change the visible URL.
- JavaMelody dependency source must be verified from `pom.xml` and dependency tree. Do not assume it is active or add/upgrade versions blindly.

## TODO items to verify
- TODO: Verify actual environment keys from source and configured deployments.
- TODO: Verify secret management rules.
- TODO: Verify file-upload-location configuration behavior.
- TODO: Verify active JavaMelody dependency source for each target build.

## Example
Do not copy datasource passwords from SQL dumps into documentation.
