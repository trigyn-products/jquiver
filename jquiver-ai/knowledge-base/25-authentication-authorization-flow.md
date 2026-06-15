# Authentication and Authorization Flow

## Purpose
Explain the authentication and authorization concepts agents must consider in JQuiver.

## When to use this file
Use this file when troubleshooting access, menus, secured APIs, public pages, file downloads, role permissions, or embedded forms.

## Related files
- `15-security-users-roles.md`
- `03-module-router.md`
- `07-dynamic-rest-api.md`
- `../reference/security-permission-matrix.md`
- `../reference/jquiver-auth-api-reference.md`
- `../skills/jquiver-database-authentication/SKILL.md`
- `../examples/jquiver-auth-postman-summary.md`
- `../playbooks/add-role-based-menu-access.md`

## Known facts
- Observed metadata includes users, roles, user-role associations, and entity-role associations.
- Some public Dynamic REST rows in analyzed dumps had `is_secured=0`.
- Public routes exist in SBI FAC for careers/job application behavior.
- `Jquiver Database Authentication.postman_collection.json` verifies auth/system endpoints including `POST {base_url}/japi/login`, captcha endpoints under `/cf/captcha/*`, OTP mail, forgot/reset password, registration, and authenticated test/security calls.

## Conceptual flow
1. User or anonymous visitor requests a route/API/file.
2. Runtime identifies whether authentication is required.
3. If authenticated, user identity and roles are loaded.
4. Route/API/entity permissions are evaluated.
5. Page or API response is allowed or rejected.
6. UI may hide or show menu items/actions based on permissions.

## Important checks
- Is the route public or authenticated?
- Does the route appear in menu?
- Does the target metadata require role/entity permission?
- Are API calls from the page also secured?
- Are file download URLs protected?
- Does embedded Form.io access bypass normal layout/security assumptions?

## Verified database authentication flows
- Password login uses `POST {base_url}/japi/login`.
- Password + captcha login first uses `GET {base_url}/cf/captcha/loginCaptcha`.
- Email OTP flows use `GET {base_url}/cf/saveOtpAndSendMail?email={email}` before completing login.
- Email OTP + captcha combines OTP prerequisite handling with login captcha handling.
- TOTP and TOTP + captcha are present in the verified collection; confirm target-instance TOTP setup before testing.
- Forgot password uses `POST {base_url}/cf/sendResetPasswordMail`.
- Reset password uses `POST {base_url}/cf/createPassword?token={token}`, with a captcha variant in the collection.
- Registration uses `POST {base_url}/cf/register` with password, OTP, and TOTP variants.
- Register captcha uses `GET {base_url}/cf/captcha/registerCaptcha`.
- Authenticated test/security endpoints require placeholder token/header handling only.

## Prefix rules
- `/japi` appears as a JQuiver API/auth endpoint prefix in the verified collection.
- `/cf` appears in the collection for captcha, OTP, registration, forgot/reset password, and some test endpoints.
- Do not assume `/cf` is the router prefix.
- Router/page links still use configured `view.path` from `application.yml` or `application.yaml`, default `/view`.
- Dynamic REST/API links still use configured `api.path` from `application.yml` or `application.yaml`, default `/api`.
- Auth/system endpoints such as `/japi/login` and `/cf/captcha/*` must be handled as verified JQuiver security/auth endpoints.

## Safe AI-agent usage
- Do not assume route visibility equals authorization.
- Verify API security separately from page security.
- Treat anonymous forms as high-risk.
- Do not expose user/security fields from dumps.
- Replace Postman sample credentials, bearer tokens, `ck`/`at` headers, reset tokens, OTP values, captcha values, encrypted payloads, emails, personal names, and localhost URLs with placeholders.
- Recommend security review for public APIs and file routes.

## TODO items to verify
- TODO: verify Keycloak/session support from source code.
- TODO: verify exact authentication modes and configuration keys.
- TODO: verify permission evaluation order.
- TODO: verify anonymous user behavior.

## Example
A public application form may be allowed anonymously, but its resume download API should not be assumed public without explicit verification.
