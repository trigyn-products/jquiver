# KB-MAP Addition: Send Mail from a Server-Side Script

Add the following task-specific reading path to `jquiver-ai/KB-MAP.md`.

## Send Mail from a Server-Side Script

Read in this order:

1. `AGENTS.md`
2. `README.md`
3. `reference/ai-agent-review-checklist.md`
4. `knowledge-base/17-notifications-mail.md`
5. `knowledge-base/32-script-library.md`
6. `reference/jquiver-mail-xml-reference.md`
7. `examples/send-mail-example.md`
8. `maven-common-utils/java-starter-webui/src/Templates/script-util.tgn`
9. `maven-common-utils/java-web-starter/src/main/java/com/trigyn/jws/dynarest/cipher/utils/ScriptUtil.java`

### Required rules

- Treat mail sending as side-effecting.
- Do not execute a mail call during analysis or documentation work.
- Confirm that the target JQuiver version exposes:
  - `jq_sendMail`
  - `jq_sendMailWithMap`
- Preserve the mail XML element names required by the current contract:
  - `recepients`
  - `recepient`
  - `failedrecepients`
  - `failedrecepient`
- Select one actual attribute value instead of copying pipe-separated documentation alternatives.
- Use `jq_sendMailWithMap` when stored or inline template content needs parameter processing.
- Verify template names and map keys in the target instance.
- Do not invent attachment identifiers, paths, or embedded-image behaviour.
- Use placeholder email addresses in generated examples.
- Validate all recipients and review privacy implications before sending.
- Mark unknown behaviour as:
  `TODO: Verify from actual JQuiver source code / database / instance export.`

### Completion criteria

A mail implementation is incomplete unless it includes, where applicable:

1. verified business trigger;
2. verified recipient source and recipient types;
3. valid JQuiver mail XML;
4. correct body content mode;
5. verified template and parameter map;
6. safe attachment handling;
7. approved sender/default mail configuration;
8. error and failure handling;
9. side-effect review;
10. development testing with non-production recipients.
