# JQuiver Send Mail Examples

## Purpose

Provide reusable examples for sending email from JQuiver server-side JavaScript using the platform functions:

```javascript
jq_sendMail(mailXML)
jq_sendMailWithMap(mailXML, requestParams)
```

Read `../reference/jquiver-mail-xml-reference.md` before adapting these examples.

## Safety note

Sending mail is a side effect.

Do not execute these examples during analysis, metadata inspection, or documentation generation. Replace placeholder recipients and values only in an approved development or target environment.

## Example 1: Basic HTML email

Use `content="1"` when the body is already final and does not require template processing.

```javascript
var mailXML = `
<emails>
    <email>
        <recepients separatemails="false">
            <recepient type="to">
                <name>Sample User</name>
                <mailid>recipient@example.com</mailid>
            </recepient>
        </recepients>

        <subject>JQuiver test email</subject>

        <body contenttype="text/html" content="1"><![CDATA[
            <p>Hello,</p>
            <p>This is a test email sent from a JQuiver server-side script.</p>
        ]]></body>
    </email>
</emails>`;

var result = jq_sendMail(mailXML);
```

## Example 2: Plain-text email

```javascript
var mailXML = `
<emails>
    <email>
        <recepients>
            <recepient type="to">
                <name>Sample User</name>
                <mailid>recipient@example.com</mailid>
            </recepient>
        </recepients>

        <subject>Plain-text notification</subject>

        <body contenttype="text/plain" content="1"><![CDATA[
Hello,

Your request has been received.

Regards,
JQuiver
        ]]></body>
    </email>
</emails>`;

var result = jq_sendMail(mailXML);
```

## Example 3: To, CC, and BCC

```javascript
var mailXML = `
<emails>
    <email>
        <recepients separatemails="false">
            <recepient type="to">
                <name>Primary User</name>
                <mailid>primary@example.com</mailid>
            </recepient>

            <recepient type="cc">
                <name>Review Team</name>
                <mailid>review@example.com</mailid>
            </recepient>

            <recepient type="bcc">
                <name>Audit Mailbox</name>
                <mailid>audit@example.com</mailid>
            </recepient>
        </recepients>

        <subject>Request submitted</subject>

        <body contenttype="text/html" content="1"><![CDATA[
            <p>The request has been submitted successfully.</p>
        ]]></body>
    </email>
</emails>`;

var result = jq_sendMail(mailXML);
```

## Example 4: Separate email for each recipient

```javascript
var mailXML = `
<emails>
    <email>
        <recepients separatemails="true">
            <recepient type="to">
                <name>First User</name>
                <mailid>first@example.com</mailid>
            </recepient>

            <recepient type="to">
                <name>Second User</name>
                <mailid>second@example.com</mailid>
            </recepient>
        </recepients>

        <subject>Individual notification</subject>

        <body contenttype="text/html" content="1"><![CDATA[
            <p>This notification is configured for separate delivery.</p>
        ]]></body>
    </email>
</emails>`;

var result = jq_sendMail(mailXML);
```

The supplied contract states that `separatemails="true"` sends separate emails. Verify exact behaviour before using this for privacy-sensitive recipient combinations.

## Example 5: Optional sender and reply-to

```javascript
var mailXML = `
<emails>
    <email>
        <sender>
            <name>JQuiver Service Desk</name>
            <mailid>service-desk@example.com</mailid>
            <replyTo>support@example.com</replyTo>
        </sender>

        <recepients>
            <recepient type="to">
                <name>Sample User</name>
                <mailid>recipient@example.com</mailid>
            </recepient>
        </recepients>

        <subject>Support request update</subject>

        <body contenttype="text/html" content="1"><![CDATA[
            <p>Your support request has been updated.</p>
        ]]></body>
    </email>
</emails>`;

var result = jq_sendMail(mailXML);
```

The SMTP server may restrict sender override. Omit `<sender>` to use the default mail configuration.

## Example 6: High-priority mail

```javascript
var mailXML = `
<emails>
    <email>
        <header>
            <property name="X-Priority">1</property>
            <property name="x-msmail-priority">high</property>
        </header>

        <recepients>
            <recepient type="to">
                <name>Operations User</name>
                <mailid>operations@example.com</mailid>
            </recepient>
        </recepients>

        <subject>Urgent action required</subject>

        <body contenttype="text/html" content="1"><![CDATA[
            <p><strong>Urgent:</strong> Please review the pending action.</p>
        ]]></body>
    </email>
</emails>`;

var result = jq_sendMail(mailXML);
```

Priority display depends on the receiving mail system and client.

## Example 7: Stored template with parameter map

Use `content="2"` when the body value is a stored template name.

```javascript
var HashMap = Java.type("java.util.HashMap");
var requestParams = new HashMap();

requestParams.put("displayName", "Sample User");
requestParams.put("requestNumber", "REQ-1001");
requestParams.put("status", "Approved");

var mailXML = `
<emails>
    <email>
        <recepients>
            <recepient type="to">
                <name>Sample User</name>
                <mailid>recipient@example.com</mailid>
            </recepient>
        </recepients>

        <subject>Request status update</subject>

        <body contenttype="text/html" content="2"><![CDATA[
            request-status-mail
        ]]></body>
    </email>
</emails>`;

var result = jq_sendMailWithMap(mailXML, requestParams);
```

Verify that `request-status-mail` is the exact stored template name in the target JQuiver instance.

## Example 8: Inline template content with parameter map

Use `content="3"` when the body itself is template content requiring processing.

```javascript
var HashMap = Java.type("java.util.HashMap");
var requestParams = new HashMap();

requestParams.put("displayName", "Sample User");
requestParams.put("requestNumber", "REQ-1001");
requestParams.put("status", "Approved");

var mailXML = `
<emails>
    <email>
        <recepients>
            <recepient type="to">
                <name>Sample User</name>
                <mailid>recipient@example.com</mailid>
            </recepient>
        </recepients>

        <subject>Request status update</subject>

        <body contenttype="text/html" content="3"><![CDATA[
            <p>Hello \${displayName},</p>
            <p>
                Request <strong>\${requestNumber}</strong>
                is now <strong>\${status}</strong>.
            </p>
        ]]></body>
    </email>
</emails>`;

var result = jq_sendMailWithMap(mailXML, requestParams);
```

The backslash before `${...}` is required in this JavaScript template-literal example so JavaScript does not interpolate it before JQuiver receives the mail template.

Verify the exact server-side template syntax in the target instance.

## Example 9: Filesystem attachment

Use `type="2"` for an attachment read from an absolute server filesystem path.

```javascript
var mailXML = `
<emails>
    <email>
        <recepients>
            <recepient type="to">
                <name>Sample User</name>
                <mailid>recipient@example.com</mailid>
            </recepient>
        </recepients>

        <subject>Requested report</subject>

        <body contenttype="text/html" content="1"><![CDATA[
            <p>Please find the requested report attached.</p>
        ]]></body>

        <attachments>
            <attachment type="2">
                <name>monthly-report.pdf</name>
                <path>/opt/jquiver/reports/monthly-report.pdf</path>
            </attachment>
        </attachments>
    </email>
</emails>`;

var result = jq_sendMail(mailXML);
```

Do not construct the path directly from untrusted request data.

## Example 10: Internal uploaded-file attachment

The supplied XML contract states that type `1` uses a file-upload ID.

```javascript
var fileUploadId = String(requestParams.get("fileUploadId"));

var mailXML = `
<emails>
    <email>
        <recepients>
            <recepient type="to">
                <name>Sample User</name>
                <mailid>recipient@example.com</mailid>
            </recepient>
        </recepients>

        <subject>Uploaded document</subject>

        <body contenttype="text/html" content="1"><![CDATA[
            <p>The uploaded document is attached.</p>
        ]]></body>

        <attachments>
            <attachment type="1">
                <name>uploaded-document.pdf</name>
                <path>${fileUploadId}</path>
            </attachment>
        </attachments>
    </email>
</emails>`;

var result = jq_sendMail(mailXML);
```

Validate that the current user or process is authorised to access the file represented by the upload ID.

## Example 11: Multiple attachments

```javascript
var mailXML = `
<emails>
    <email>
        <recepients>
            <recepient type="to">
                <name>Sample User</name>
                <mailid>recipient@example.com</mailid>
            </recepient>
        </recepients>

        <subject>Documents</subject>

        <body contenttype="text/html" content="1"><![CDATA[
            <p>Please find the documents attached.</p>
        ]]></body>

        <attachments>
            <attachment type="2">
                <name>report.pdf</name>
                <path>/opt/jquiver/reports/report.pdf</path>
            </attachment>

            <attachment type="2">
                <name>summary.xlsx</name>
                <path>/opt/jquiver/reports/summary.xlsx</path>
            </attachment>
        </attachments>
    </email>
</emails>`;

var result = jq_sendMail(mailXML);
```

## Example 12: Multiple email definitions in one call

```javascript
var mailXML = `
<emails>
    <email>
        <recepients>
            <recepient type="to">
                <name>First User</name>
                <mailid>first@example.com</mailid>
            </recepient>
        </recepients>

        <subject>First message</subject>

        <body contenttype="text/plain" content="1"><![CDATA[
First email body.
        ]]></body>
    </email>

    <email>
        <recepients>
            <recepient type="to">
                <name>Second User</name>
                <mailid>second@example.com</mailid>
            </recepient>
        </recepients>

        <subject>Second message</subject>

        <body contenttype="text/plain" content="1"><![CDATA[
Second email body.
        ]]></body>
    </email>
</emails>`;

var result = jq_sendMail(mailXML);
```

The root contract permits multiple `<email>` elements. Verify transaction and partial-failure behaviour before relying on all-or-nothing delivery.

## Example 13: Embedded image pattern requiring verification

```javascript
var mailXML = `
<emails>
    <email>
        <recepients>
            <recepient type="to">
                <name>Sample User</name>
                <mailid>recipient@example.com</mailid>
            </recepient>
        </recepients>

        <subject>Mail with logo</subject>

        <body contenttype="text/html" content="1"><![CDATA[
            <p>Hello,</p>
            <img src="cid:companyLogo" alt="Company logo">
        ]]></body>

        <attachments>
            <attachment type="2" hasEmbeddedImage="companyLogo">
                <name>logo.png</name>
                <path>/opt/jquiver/assets/logo.png</path>
            </attachment>
        </attachments>
    </email>
</emails>`;

var result = jq_sendMail(mailXML);
```

This example reflects the apparent intent of `hasEmbeddedImage`, but the exact MIME Content-ID mapping is not verified. Confirm it from JQuiver source or a working runtime example before use.

## Example 14: Build XML safely from values

Do not place unescaped external values directly into XML.

A minimal XML escaping helper:

```javascript
function escapeXml(value) {
    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&apos;");
}

var recipientName = escapeXml(requestParams.get("recipientName"));
var recipientEmail = escapeXml(requestParams.get("recipientEmail"));

var mailXML = `
<emails>
    <email>
        <recepients>
            <recepient type="to">
                <name>${recipientName}</name>
                <mailid>${recipientEmail}</mailid>
            </recepient>
        </recepients>

        <subject>Notification</subject>

        <body contenttype="text/plain" content="1"><![CDATA[
A notification was generated.
        ]]></body>
    </email>
</emails>`;

var result = jq_sendMail(mailXML);
```

Values inserted inside CDATA require a different review. In particular, prevent untrusted content from injecting the CDATA terminator `]]>`.

## Example 15: Basic error handling

Use the error-handling style already established by the target Dynamic REST script. A generic pattern is:

```javascript
try {
    var result = jq_sendMail(mailXML);

    return {
        actionStatus: "true",
        mailResponse: result
    };
} catch (error) {
    return {
        actionStatus: "false",
        errorMessage: String(error)
    };
}
```

Do not expose stack traces, SMTP credentials, private recipients, or complete mail bodies in a client response.

## AI-agent generation checklist

Before generating or executing a mail example:

- preserve `recepients` and `recepient`;
- select real single attribute values, not pipe-separated documentation values;
- omit unused optional nodes;
- use placeholder addresses in documentation;
- use `jq_sendMailWithMap` for parameterised templates;
- escape values inserted into XML;
- verify file access for attachments;
- verify template names and parameter keys;
- do not assume embedded-image behaviour;
- do not include `failedrecepients` in a new request without verified evidence;
- treat the operation as side-effecting.
