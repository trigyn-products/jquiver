# JQuiver Auth Postman Summary

## Status
Verified from `Jquiver Database Authentication.postman_collection.json`; values below are sanitized placeholders only.

## Flow summary
- Password login: `POST {base_url}/japi/login`
- Password + captcha login: fetch `GET {base_url}/cf/captcha/loginCaptcha`, then `POST {base_url}/japi/login`
- Email OTP: request `GET {base_url}/cf/saveOtpAndSendMail?email={email}`, then complete login with `{otp}`
- Email OTP + captcha: fetch login captcha, request email OTP, then complete login with `{otp}` and `{captcha}`
- TOTP: complete login with TOTP values after verifying target-instance setup
- TOTP + captcha: fetch login captcha, then complete TOTP login with `{captcha}`
- Forgot password: `POST {base_url}/cf/sendResetPasswordMail`
- Reset password: `POST {base_url}/cf/createPassword?token={token}`
- Reset password + captcha: fetch captcha when required, then call reset with `{token}` and `{captcha}`
- Registration: `POST {base_url}/cf/register` for password, OTP, and TOTP variants
- Register captcha: `GET {base_url}/cf/captcha/registerCaptcha`
- Authenticated test/security endpoint: use placeholder auth headers/tokens only

## Header/token placeholders
```text
Authorization: Bearer {bearer_token}
at: {at_header}
ck: {ck_header}
```

## Safety notes
- Do not reuse Postman sample usernames, passwords, emails, tokens, captcha values, OTP values, encrypted payloads, localhost URLs, or personal names.
- Treat collection values as sample placeholders only.
- Do not call mail, registration, reset-password, or security test endpoints against production without explicit authorization.

## Prefix reminder
`/japi` and `/cf` are verified auth/system endpoint prefixes in this collection. They do not replace configured router `view.path` or Dynamic REST `api.path`.
