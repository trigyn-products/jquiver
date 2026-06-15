# JavaMelody Monitoring Example

## Status
Safe placeholder example based on the verified archetype pattern. Do not copy production diagnostics into this file.

## Verified archetype pattern
- `application.yml` includes an optional commented `javamelody` block.
- When the block is commented, JavaMelody is available to configure but disabled.
- Default monitoring path is `/monitoring`, configurable with `javamelody.monitoring-path`.
- Router and Dynamic REST prefixes remain separate: `view.path` defaults to `/view`, and `api.path` defaults to `/api`.

## Safe documentation shape
```text
Config file: application.yml
JavaMelody config: commented / enabled / absent
Dependency status: explicit / starter-provided TODO / absent TODO
Expected path: {context_path}{monitoring_path}
Access control: authentication / IP restriction / VPN / reverse proxy protection TODO
Verification: start app in approved environment and confirm sanitized reachability only
```

## Do not include
- Real monitoring URLs.
- JavaMelody screenshots or exported output.
- SQL traces, request parameters, session data, user data, stack traces, credentials, or production diagnostics.
