# JQuiver Auth API Reference

## Purpose
Summarize verified JQuiver database authentication endpoints from `Jquiver Database Authentication.postman_collection.json` at a safe placeholder-only level.

## Verified auth/system endpoints
- `POST {base_url}/japi/login` - password, password + captcha, OTP, and TOTP login variants.
- `GET {base_url}/cf/captcha/loginCaptcha` - login captcha prerequisite.
- `GET {base_url}/cf/saveOtpAndSendMail?email={email}` - email OTP prerequisite.
- `POST {base_url}/cf/sendResetPasswordMail` - forgot-password mail flow.
- `POST {base_url}/cf/createPassword?token={token}` - reset-password flow, with optional captcha variant.
- `POST {base_url}/cf/register` - registration flow with password, OTP, and TOTP variants.
- `GET {base_url}/cf/captcha/registerCaptcha` - registration captcha prerequisite.

## Prefix rules
- `/japi` appears as a JQuiver API/auth endpoint prefix in the verified collection.
- `/cf` appears in the collection for captcha, OTP, registration, forgot/reset password, and some test endpoints.
- Do not assume `/cf` is the router prefix.
- Router/page links still use configured `view.path` from `application.yml` or `application.yaml`, default `/view`.
- Dynamic REST/API links still use configured `api.path` from `application.yml` or `application.yaml`, default `/api`.
- Auth/system endpoints such as `/japi/login` and `/cf/captcha/*` are security/auth flows, not Dynamic REST examples.

## Placeholder rules
Use only these placeholders in examples:
- `{base_url}`
- `{username}`
- `{password}`
- `{email}`
- `{captcha}`
- `{otp}`
- `{token}`
- `{at_header}`
- `{ck_header}`
- `{bearer_token}`

Never preserve real sample passwords, bearer tokens, `ck` values, `at` values, reset tokens, encrypted payloads, emails, localhost ports, or personal names from the collection.

## Safe request shapes
```text
POST {base_url}/japi/login
GET  {base_url}/cf/captcha/loginCaptcha
GET  {base_url}/cf/saveOtpAndSendMail?email={email}
POST {base_url}/cf/sendResetPasswordMail
POST {base_url}/cf/createPassword?token={token}
POST {base_url}/cf/register
GET  {base_url}/cf/captcha/registerCaptcha
```

## TODO items to verify
- TODO: Verify exact request/response field names from current JQuiver source code before implementing clients.
- TODO: Verify configured authentication mode and password/OTP/TOTP policy in the target instance.
- TODO: Verify exact authenticated test/security endpoint behavior before invoking it.
