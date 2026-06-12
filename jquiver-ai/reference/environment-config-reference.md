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

## TODO items to verify
- TODO: Verify actual environment keys from source and configured deployments.
- TODO: Verify secret management rules.
- TODO: Verify file-upload-location configuration behavior.

## Example
Do not copy datasource passwords from SQL dumps into documentation.
