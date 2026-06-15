---
name: jquiver-database-authentication
description: Use for JQuiver database authentication, login, captcha, email OTP, TOTP, forgot password, reset password, registration, auth token/header handling, and security/auth endpoints verified from the Postman collection.
---

# JQuiver Database Authentication

## 1. Purpose
Guide safe analysis, documentation, and testing of JQuiver database authentication flows verified from `Jquiver Database Authentication.postman_collection.json`.

## 2. When to use
Use this skill for login, registration, forgot/reset password, OTP, TOTP, captcha, `/japi/login`, `/cf/captcha/*`, `/cf/register`, and authenticated security/API test calls.

## 3. Files to read first
- `../../knowledge-base/25-authentication-authorization-flow.md`
- `../../knowledge-base/15-security-users-roles.md`
- `../../reference/jquiver-auth-api-reference.md`
- `../../examples/jquiver-auth-postman-summary.md`
- `../../reference/environment-config-reference.md`
- `../../developer-runbook/safe-data-handling.md`

## 4. Supported auth flows
- Password login.
- Password + captcha login.
- Email OTP.
- Email OTP + captcha.
- TOTP.
- TOTP + captcha.
- Forgot password.
- Reset password.
- Reset password + captcha.
- Registration with password, OTP, and TOTP variants.
- Register captcha and login captcha.
- Authenticated test/security endpoint calls using token/header values.

## 5. Endpoint summary
- `POST {base_url}/japi/login`
- `GET {base_url}/cf/captcha/loginCaptcha`
- `GET {base_url}/cf/saveOtpAndSendMail?email={email}`
- `POST {base_url}/cf/sendResetPasswordMail`
- `POST {base_url}/cf/createPassword?token={token}`
- `POST {base_url}/cf/register`
- `GET {base_url}/cf/captcha/registerCaptcha`

Treat `/japi` and `/cf` here as verified JQuiver security/auth endpoint prefixes from the Postman collection. Do not infer Dynamic REST or router prefixes from them.

## 6. Required pre-request flows
- Fetch captcha before captcha-protected login, registration, or reset-password calls.
- Request email OTP before OTP login or OTP registration.
- Confirm TOTP enrollment/setup behavior from source or instance before testing TOTP flows.
- Get a fresh reset token through the forgot-password flow before reset-password testing.

## 7. Header/token handling
- Use placeholders in documentation: `{bearer_token}`, `{at_header}`, `{ck_header}`, and `{token}`.
- Treat bearer tokens, `ck`, `at`, reset tokens, encrypted payloads, and session values as secrets.
- Do not copy headers from Postman examples into generated docs or scripts.

## 8. Captcha/OTP/TOTP handling
- Use `{captcha}`, `{otp}`, and `{token}` placeholders.
- Never document real captcha answers, OTP values, TOTP seeds/codes, reset tokens, or encrypted request bodies.
- Mark collection values as sample placeholders only.

## 9. Safe test procedure
1. Use a non-production instance and test account.
2. Replace all credentials, emails, tokens, and headers with placeholders in notes.
3. Run only the minimum prerequisite calls needed for the target flow.
4. Avoid sending real password reset or OTP mail unless explicitly authorized.
5. Verify success/failure behavior without storing sensitive response values.

## 10. Security rules
- Do not expose or reuse sample credentials, tokens, `ck`/`at` headers, reset tokens, OTP values, captcha values, encrypted payloads, emails, personal names, or localhost URLs from Postman collections.
- Use `{username}`, `{password}`, `{email}`, `{captcha}`, `{otp}`, `{token}`, `{base_url}`, `{at_header}`, `{ck_header}`, and `{bearer_token}`.
- Do not commit executable credentials or real tokens into examples.

## 11. Output format
Return:
- Flow being handled.
- Endpoint(s) used with placeholders.
- Required pre-request calls.
- Required headers/tokens as placeholders.
- Security notes.
- TODOs for source/config/runtime verification.

## 12. Things not to do
- Do not treat `/cf` as the router prefix.
- Do not treat `/japi/login` or `/cf/captcha/*` as Dynamic REST APIs.
- Do not hardcode `/view`, `/api`, `/cf`, or localhost URLs without source/config/runtime verification.
- Do not call mail, reset, registration, or authenticated side-effect endpoints in production without explicit authorization.
