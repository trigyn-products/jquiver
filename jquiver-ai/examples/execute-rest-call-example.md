# JQuiver REST Call Examples

## Purpose

Provide reusable examples for invoking REST or HTTP endpoints from JQuiver server-side JavaScript using:

```javascript
jq_executeRESTCall(restXML)
```

Read `../reference/jquiver-rest-xml-reference.md` before adapting these examples.

## Safety note

A REST call can read, create, update, delete, upload, or trigger processing in another system.

Do not execute these examples during analysis or documentation generation. Use only approved development endpoints and placeholder values. Do not insert real tokens, credentials, private URLs, or unrestricted file paths.

## Example 1: GET request with headers and query parameters

```javascript
var restXML = `
<rest>
    <verifySSL>true</verifySSL>
    <url>https://api.example.com/v1/items</url>
    <type>GET</type>
    <content-type>application/json</content-type>
    <request>
        <headers>
            <parameter>
                <name>Accept</name>
                <value>application/json</value>
            </parameter>
            <parameter>
                <name>X-Correlation-ID</name>
                <value>REQ-1001</value>
            </parameter>
        </headers>
        <query-param>
            <parameter>
                <name>status</name>
                <value>ACTIVE</value>
            </parameter>
            <parameter>
                <name>page</name>
                <value>1</value>
            </parameter>
        </query-param>
    </request>
</rest>`;

var response = jq_executeRESTCall(restXML);
```

## Example 2: POST with key-value body

```javascript
var restXML = `
<rest>
    <verifySSL>true</verifySSL>
    <url>https://api.example.com/v1/items</url>
    <type>POST</type>
    <content-type>application/json</content-type>
    <request>
        <headers>
            <parameter>
                <name>Accept</name>
                <value>application/json</value>
            </parameter>
        </headers>
        <body contentType="keyValue">
            <parameter>
                <name>name</name>
                <value>Sample Item</value>
            </parameter>
            <parameter>
                <name>active</name>
                <value>true</value>
            </parameter>
        </body>
    </request>
</rest>`;

var response = jq_executeRESTCall(restXML);
```

Verify whether plain values are converted to JSON booleans/numbers or remain strings.

## Example 3: POST raw JSON body

Use `data-raw` when `contentType="rawBody"`.

```javascript
var restXML = `
<rest>
    <verifySSL>true</verifySSL>
    <url>https://api.example.com/v1/items</url>
    <type>POST</type>
    <content-type>application/json</content-type>
    <request>
        <headers>
            <parameter>
                <name>Accept</name>
                <value>application/json</value>
            </parameter>
        </headers>
        <body contentType="rawBody">
            <parameter>
                <name>data-raw</name>
                <value><![CDATA[
{
  "name": "Sample Item",
  "active": true,
  "tags": ["demo", "jquiver"]
}
                ]]></value>
            </parameter>
        </body>
    </request>
</rest>`;

var response = jq_executeRESTCall(restXML);
```

Do not add `<attachments>` to a raw-body request.

## Example 4: JSON-typed nested parameter

```javascript
var restXML = `
<rest>
    <verifySSL>true</verifySSL>
    <url>https://api.example.com/v1/users</url>
    <type>POST</type>
    <content-type>application/json</content-type>
    <request>
        <body contentType="keyValue">
            <parameter>
                <name>displayName</name>
                <value>Sample User</value>
            </parameter>
            <parameter dataType="json">
                <name>profile</name>
                <value><![CDATA[
{
  "department": "Technology",
  "roles": ["reviewer", "approver"]
}
                ]]></value>
            </parameter>
        </body>
    </request>
</rest>`;

var response = jq_executeRESTCall(restXML);
```

TODO: Verify exact `dataType="json"` parsing and nesting behaviour in the target runtime.

## Example 5: Authentication selector

OAuth selector example:

```javascript
var restXML = `
<rest>
    <verifySSL>true</verifySSL>
    <url>https://api.example.com/v1/secure/items</url>
    <type>GET</type>
    <content-type>application/json</content-type>
    <request>
        <headers>
            <parameter>
                <name>at</name>
                <value>7d1oa821</value>
            </parameter>
            <parameter>
                <name>Accept</name>
                <value>application/json</value>
            </parameter>
        </headers>
    </request>
</rest>`;

var response = jq_executeRESTCall(restXML);
```

Other documented selectors:

```text
7d1dba821  Database authentication
7d1ldap821 LDAP authentication
```

Use one selector only. Verify the configured authentication flow and never hard-code credentials or bearer tokens.

## Example 6: Internal JQuiver API pattern

The Java source says internal API URLs should use `JAPI` rather than `API` as the prefix.

```javascript
var restXML = `
<rest>
    <verifySSL>true</verifySSL>
    <url>JAPI/TODO-VERIFY-INTERNAL-PATH</url>
    <type>GET</type>
    <content-type>application/json</content-type>
    <request>
        <headers>
            <parameter>
                <name>Accept</name>
                <value>application/json</value>
            </parameter>
        </headers>
    </request>
</rest>`;

var response = jq_executeRESTCall(restXML);
```

Replace the placeholder only after confirming the exact internal URL syntax and endpoint in the target JQuiver version.

## Example 7: Filesystem attachment

```javascript
var restXML = `
<rest>
    <verifySSL>true</verifySSL>
    <url>https://api.example.com/v1/documents</url>
    <type>POST</type>
    <content-type>multipart/form-data</content-type>
    <request>
        <body contentType="keyValue">
            <parameter>
                <name>description</name>
                <value>Sample document</value>
            </parameter>
        </body>
        <attachments>
            <attachment type="2">
                <name>sample-document.pdf</name>
                <path>/opt/jquiver/approved-uploads/sample-document.pdf</path>
            </attachment>
        </attachments>
    </request>
</rest>`;

var response = jq_executeRESTCall(restXML);
```

Do not construct filesystem paths directly from request parameters. Verify the parser-generated multipart field name and boundary behaviour.

## Example 8: Internal uploaded-file attachment

```javascript
var fileUploadId = "TODO-VALIDATED-FILE-UPLOAD-ID";

var restXML = `
<rest>
    <verifySSL>true</verifySSL>
    <url>https://api.example.com/v1/documents</url>
    <type>POST</type>
    <content-type>multipart/form-data</content-type>
    <request>
        <attachments>
            <attachment type="1">
                <name>uploaded-document.pdf</name>
                <path>${fileUploadId}</path>
            </attachment>
        </attachments>
    </request>
</rest>`;

var response = jq_executeRESTCall(restXML);
```

Validate the upload ID and authorisation before building the XML.

## Example 9: Escape external values before inserting into XML

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

var status = escapeXml(requestParams.get("status"));
var correlationId = escapeXml(requestParams.get("correlationId"));

var restXML = `
<rest>
    <verifySSL>true</verifySSL>
    <url>https://api.example.com/v1/items</url>
    <type>GET</type>
    <content-type>application/json</content-type>
    <request>
        <headers>
            <parameter>
                <name>X-Correlation-ID</name>
                <value>${correlationId}</value>
            </parameter>
        </headers>
        <query-param>
            <parameter>
                <name>status</name>
                <value>${status}</value>
            </parameter>
        </query-param>
    </request>
</rest>`;

var response = jq_executeRESTCall(restXML);
```

XML escaping does not replace URL allow-listing, header validation, authentication controls, or endpoint authorisation.

## Example 10: Response handling without assuming properties

```javascript
var response = jq_executeRESTCall(restXML);

if (response === null || response === undefined) {
    throw new Error("REST call returned no response");
}

// TODO: Inspect and verify the CustomResponseEntity properties exposed
// by the target JQuiver script engine before accessing response fields.
```

Do not assume a response structure based only on common HTTP-client conventions.

## Unsupported or incomplete patterns

Do not generate these as production-ready without further verification:

- attachment type `3`;
- `hasEmbeddedImage` behaviour in REST calls;
- raw body with attachments;
- timeout values labelled with a unit;
- direct property access on the response object;
- disabled SSL verification;
- arbitrary user-controlled URLs or file paths;
- real bearer tokens or API keys in XML.
