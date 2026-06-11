# SBI FAC Instance

## Purpose
Summarize verified SBI FAC instance knowledge and safe analysis guidance.

## When to use this file
Use this file when analyzing or documenting the SBI FAC JQuiver instance or custom schema.

## Related files
- `13-additional-datasource.md`
- `07-dynamic-rest-api.md`
- `31-autocomplete-typeahead.md`
- `../examples/sbi-fac-instance-summary.md`

## Known facts
- The public `/sbi/` URL returned a careers page over HTTP.
- Public flow includes job listing, job description, OTP/captcha, and application details.
- Custom schema includes `jd_details`, `applicant_details`, lookup tables, and `jd_otp`.
- Main schema includes interview management and JQuiver metadata.
- Interview management tables include candidate details, interview details, feedback, HR feedback, panels, practice, question, rating, type, and decision lookup concepts.
- SBI FAC includes file upload bin `applicantresume_fb`.
- Applicant data and resumes contain PII.

## Domain concepts
SBI FAC appears to combine:
- Public careers/job listing.
- Public applicant application flow.
- OTP/captcha validation.
- Resume upload.
- Internal applicant/job listing.
- Interview scheduling.
- Panel and interviewer management.
- Interview assessment and feedback.
- Dashboard metrics for jobs, applicants, interviews, selected candidates, and panels.

## Safe AI-agent usage
- Do not expose applicant names, email addresses, phone numbers, resumes, or raw application JSON.
- Do not call OTP, save, reminder, email, or scheduler APIs without explicit instruction.
- Verify security before treating routes as public.
- Use aggregate counts and schema summaries instead of raw applicant data.

## TODO items to verify
- TODO: verify SBI runtime UI behavior with a working browser bridge.
- TODO: verify production authentication and authorization rules.
- TODO: verify all Dynamic REST API side effects before invoking them.
- TODO: verify applicant JSON field contract from source or UI.

## Example
For applicant listing issues, inspect route metadata, `jd-applicantsGrid`, `view_jd_applicants`, custom datasource configuration, and privacy constraints.

