# External Form Embedding

## Purpose
Explain how JQuiver pluggable forms can be embedded in external applications.

## When to use this file
Use this file for surveys, feedback forms, public forms, Form.io forms, anonymous forms, or external application embedding.

## Related files
- `05-formio-pluggable-forms.md`
- `25-authentication-authorization-flow.md`
- `../examples/formio-embed-single-submit.html`
- `../examples/formio-embed-multi-submit.html`

## Known facts
- ARK examples use simple iframe embedding.
- Single-submit example uses `/arkkt/view/cf`.
- Multi-submit example uses `/arkkt/view/cfs`.
- The example pages use standalone HTML with a full-width iframe and `600px` height.

## Embed lifecycle
1. External application renders an iframe.
2. Iframe points to a JQuiver route.
3. JQuiver renders the pluggable form.
4. User submits the form inside the iframe.
5. JQuiver handles persistence and response behavior.

## Safety and integration concerns
- Authentication and anonymous access.
- Frame headers and browser security restrictions.
- Cross-origin behavior.
- Submission mode.
- Data privacy.
- Captcha or spam prevention for public forms.
- Branding and responsive sizing in the host page.

## Safe AI-agent usage
- Do not assume a route is embeddable until frame/security behavior is verified.
- Do not assume public submissions are allowed.
- Verify whether host page and JQuiver are same-origin or cross-origin.
- Recommend security review for anonymous forms.

## TODO items to verify
- TODO: verify supported embed parameters.
- TODO: verify CORS, frame, and authentication requirements.
- TODO: verify public anonymous access rules.
- TODO: verify submission completion and redirect behavior.

## Example
An external feedback page can embed a JQuiver form route in an iframe, but the agent must verify whether the route supports anonymous access and whether browser frame policies allow embedding.

