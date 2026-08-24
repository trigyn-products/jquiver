# KB-MAP Addition: Execute REST Calls from a Server-Side Script

Add the following task-specific reading path to `jquiver-ai/KB-MAP.md`.

## Execute a REST or HTTP Call from a Server-Side Script

Read in this order:

1. `AGENTS.md`
2. `README.md`
3. `reference/ai-agent-review-checklist.md`
4. `knowledge-base/32-script-library.md`
5. `knowledge-base/33-rest-http-integration.md`
6. `reference/jquiver-rest-xml-reference.md`
7. `examples/execute-rest-call-example.md`
8. `maven-common-utils/java-starter-webui/src/Templates/script-util.tgn`
9. `maven-common-utils/java-web-starter/src/main/java/com/trigyn/jws/dynarest/cipher/utils/ScriptUtil.java`

### Required rules

- Confirm that the target JQuiver version exposes `jq_executeRESTCall`.
- Treat the call as potentially side-effecting.
- Do not execute a REST call during analysis or documentation work.
- Validate or allow-list target protocols, hosts, ports, and paths.
- Preserve SSL verification unless a controlled exception is explicitly approved.
- Do not hard-code credentials, bearer tokens, cookies, API keys, or secrets.
- Include nonblank `<url>` and `<type>` values.
- Select one actual attribute or element value instead of copying pipe-separated documentation alternatives.
- Use `data-raw` for `contentType="rawBody"`.
- Do not combine raw-body mode with attachments.
- Escape XML values or use CDATA for raw content.
- Verify timeout units before describing or setting them.
- Verify the exact response object before accessing fields.
- Do not invent internal JAPI paths, file-upload IDs, filesystem paths, or type `3` attachment bindings.
- Mark unknown behaviour as:
  `TODO: Verify from actual JQuiver source code / REST XML parser / target instance.`

### Completion criteria

A REST-call implementation is incomplete unless it includes, where applicable:

1. verified business trigger;
2. approved endpoint or internal JAPI path;
3. supported HTTP method;
4. SSL decision;
5. timeout with verified unit;
6. validated headers and authentication source;
7. query parameters;
8. content type and body mode;
9. correct XML escaping or CDATA;
10. safe attachment handling;
11. response-shape verification;
12. safe error and log handling;
13. side-effect and retry review;
14. SSRF, secret, privacy, and authorisation review;
15. testing against a non-production endpoint.
