# JQuiver Script Utility Reference

This document describes the built-in `jq_*` utility functions exposed to JQuiver scripts through `script-util.tgn`.

## Source Files

- JavaScript wrapper:  
  <https://github.com/trigyn-products/jquiver/blob/master/maven-common-utils/java-starter-webui/src/Templates/script-util.tgn>

- Java implementation:  
  <https://github.com/trigyn-products/jquiver/blob/master/maven-common-utils/java-web-starter/src/main/java/com/trigyn/jws/dynarest/cipher/utils/ScriptUtil.java>

The wrapper creates an instance of:

```javascript
Java.type('com.trigyn.jws.dynarest.cipher.utils.ScriptUtil')
```

The methods documented below can be used from supported JQuiver server-side script contexts.

---

## Important Implementation Notes

### Cookie-backed session methods

The current Java implementation no longer uses the HTTP session for the following methods:

- `jq_updateSession`
- `jq_getValueFromSession`
- `jq_haveSessionKey`
- `jq_deleteSessionKey`

These methods currently store and retrieve values through browser cookies.

### Inactive session-time methods

The following functions exist in `script-util.tgn`, but their Java calls are commented out:

```javascript
jq_getCreationTime()
jq_getLastAccessedTime()
```

They currently return `undefined` and should not be used until their implementation is restored.

### Duplicate JavaScript function declarations

`script-util.tgn` declares multiple functions with the same JavaScript name:

```javascript
jq_updateCookies(...)
jq_copyFile(...)
```

In standard JavaScript behaviour, the last declaration may replace earlier declarations. The safest currently declared forms are:

```javascript
jq_updateCookies(key, value, maxAge, httpOnly)
jq_copyFile(sourceFilePath, destinationFilePath, targetFileName)
```

Runtime behaviour should be verified against the JavaScript engine used by the deployed JQuiver version.

### Response maps

Many file and database methods return a Java `Map`, exposed to the script as an object. Common response fields include:

```javascript
{
    actionStatus: "true" | "false",
    _error: "Error details when unsuccessful"
}
```

Some methods add fields such as:

- `data_list`
- `affectedRowCount`
- File-specific information

Always check `actionStatus` before using the returned data.

---

# 1. System Properties and Environment

## `jq_getSystemProperty`

Returns a Java system property.

```javascript
jq_getSystemProperty(a_propertyName)
```

### Parameters

| Parameter | Description |
|---|---|
| `a_propertyName` | Name of the Java system property |

### Example

```javascript
var javaVersion = jq_getSystemProperty("java.version");
```

---

## `jq_getSystemEnvironment`

Returns an operating-system environment variable.

```javascript
jq_getSystemEnvironment(a_propertyName)
```

### Parameters

| Parameter | Description |
|---|---|
| `a_propertyName` | Name of the environment variable |

### Example

```javascript
var homePath = jq_getSystemEnvironment("HOME");
```

---

# 2. Cookie Utilities

## `jq_updateCookies`

Creates or updates a cookie.

Declared wrapper signatures:

```javascript
jq_updateCookies(a_strKey, a_strValue)
jq_updateCookies(a_strKey, a_strValue, maxAge)
jq_updateCookies(a_strKey, a_strValue, httpOnly)
jq_updateCookies(a_strKey, a_strValue, maxAge, httpOnly)
```

### Parameters

| Parameter | Description |
|---|---|
| `a_strKey` | Cookie name |
| `a_strValue` | Cookie value |
| `maxAge` | Optional maximum age in seconds |
| `httpOnly` | Optional HTTP-only flag |

The Java implementation sets the cookie path to `/`.

### Recommended usage

```javascript
jq_updateCookies("userPreference", "compact", 3600, true);
```

> Because the wrapper declares the same JavaScript function name multiple times, use the four-parameter form unless runtime behaviour has been verified.

---

## `jq_updateCookieSecurity`

Changes the `Secure` flag of an existing cookie.

```javascript
jq_updateCookieSecurity(a_strKey, isSecured)
```

### Parameters

| Parameter | Description |
|---|---|
| `a_strKey` | Cookie name |
| `isSecured` | `true` to mark the cookie secure; otherwise `false` |

### Example

```javascript
jq_updateCookieSecurity("userPreference", true);
```

---

## `jq_getCookiesFromRequest`

Returns the cookie object matching the supplied name.

```javascript
jq_getCookiesFromRequest(a_strKey)
```

### Example

```javascript
var cookie = jq_getCookiesFromRequest("userPreference");

if (cookie != null) {
    var value = cookie.getValue();
}
```

---

## `jq_haveCookie`

Checks whether a cookie exists.

```javascript
jq_haveCookie(a_strKey)
```

### Returns

`true` when the cookie exists; otherwise `false`.

### Example

```javascript
if (jq_haveCookie("userPreference")) {
    // Cookie is available.
}
```

---

## `jq_deleteCookie`

Deletes a cookie by setting its maximum age to zero.

```javascript
jq_deleteCookie(a_strKey)
```

### Example

```javascript
jq_deleteCookie("userPreference");
```

---

# 3. Session-Compatible Utilities

These methods currently operate through cookies rather than the HTTP session.

## `jq_updateSession`

Stores a key and value through the cookie-backed session-compatible interface.

```javascript
jq_updateSession(a_strKey, a_strValue)
```

### Example

```javascript
jq_updateSession("selectedProject", "PROJECT-1001");
```

---

## `jq_getValueFromSession`

Retrieves a value from the cookie-backed session-compatible interface.

```javascript
jq_getValueFromSession(a_strKey)
```

### Example

```javascript
var projectId = jq_getValueFromSession("selectedProject");
```

---

## `jq_haveSessionKey`

Checks whether a key exists in the cookie-backed session-compatible interface.

```javascript
jq_haveSessionKey(a_strKey)
```

### Example

```javascript
if (jq_haveSessionKey("selectedProject")) {
    // Value is available.
}
```

---

## `jq_deleteSessionKey`

Removes a key from the cookie-backed session-compatible interface.

```javascript
jq_deleteSessionKey(a_strKey)
```

### Example

```javascript
jq_deleteSessionKey("selectedProject");
```

---

## `jq_getCreationTime`

Currently inactive.

```javascript
jq_getCreationTime()
```

The call to the Java implementation is commented out in `script-util.tgn`. The function currently returns `undefined`.

---

## `jq_getLastAccessedTime`

Currently inactive.

```javascript
jq_getLastAccessedTime()
```

The call to the Java implementation is commented out in `script-util.tgn`. The function currently returns `undefined`.

---

# 4. File-System and File-Bin Utilities

## `jq_getAllFiles`

Returns information about files contained in a directory.

```javascript
jq_getAllFiles(a_filePath)
```

### Parameters

| Parameter | Description |
|---|---|
| `a_filePath` | Absolute or server-accessible directory path |

### Example

```javascript
var result = jq_getAllFiles("/opt/jquiver/import");

if (result.actionStatus == "true") {
    // Process returned file information.
}
```

---

## `jq_deleteFile`

Deletes a physical file.

```javascript
jq_deleteFile(a_filePath)
```

### Example

```javascript
var result = jq_deleteFile("/opt/jquiver/temp/sample.txt");
```

> Validate and restrict all paths supplied by users. Do not pass untrusted file paths directly.

---

## `jq_saveFile`

Writes string content to a file under the configured JQuiver file-upload location.

```javascript
jq_saveFile(a_strFileContent, a_strTargetFileName)
```

### Parameters

| Parameter | Description |
|---|---|
| `a_strFileContent` | Text content to write |
| `a_strTargetFileName` | Target filename, including extension |

The Java implementation does not overwrite an existing file with the same target name.

### Example

```javascript
var result = jq_saveFile("Sample content", "sample.txt");
```

---

## `jq_saveFileFromPath`

Saves an existing physical file into a configured JQuiver file bin.

```javascript
jq_saveFileFromPath(a_strFilePath, a_strFileBinID, a_strcontextID)
```

### Parameters

| Parameter | Description |
|---|---|
| `a_strFilePath` | Source file path |
| `a_strFileBinID` | Target file-bin identifier |
| `a_strcontextID` | Business or application context identifier |

### Example

```javascript
var result = jq_saveFileFromPath(
    "/opt/jquiver/import/invoice.pdf",
    "INVOICE_BIN",
    "INV-1001"
);
```

---

## `jq_saveFileBin`

Saves supplied file content into a configured JQuiver file bin.

```javascript
jq_saveFileBin(
    a_strFileContent,
    a_strTargetFileName,
    a_strFileBinID,
    a_strcontextID
)
```

### Example

```javascript
var result = jq_saveFileBin(
    fileContent,
    "invoice.pdf",
    "INVOICE_BIN",
    "INV-1001"
);
```

---

## `jq_getFileContent`

Reads the content of a physical file.

```javascript
jq_getFileContent(a_strAbsolutePath)
```

### Example

```javascript
var result = jq_getFileContent("/opt/jquiver/import/sample.txt");
```

> File access is performed on the server. Use only trusted and validated paths.

---

## `jq_getFileBinContent`

Returns the content or content-related information for a file-bin upload record.

```javascript
jq_getFileBinContent(a_strfileUploadID)
```

### Example

```javascript
var result = jq_getFileBinContent("FILE-UPLOAD-ID");
```

---

## `jq_copyFile`

Copies a physical file.

Declared wrapper signatures:

```javascript
jq_copyFile(sourceFilePath, destinationFilePath)
jq_copyFile(sourceFilePath, destinationFilePath, a_strTargetFileName)
```

### Parameters

| Parameter | Description |
|---|---|
| `sourceFilePath` | Source file path |
| `destinationFilePath` | Destination directory or path |
| `a_strTargetFileName` | Optional target filename |

### Recommended usage

```javascript
var result = jq_copyFile(
    "/opt/jquiver/import/source.pdf",
    "/opt/jquiver/archive",
    "archived-source.pdf"
);
```

> Because the wrapper declares the same JavaScript function name twice, use the three-parameter form unless runtime behaviour has been verified.

---

## `jq_copyFileBinId`

Copies a file identified by its file-upload ID to a destination path.

```javascript
jq_copyFileBinId(a_strfileUploadID, destinationFilePath)
```

### Example

```javascript
var result = jq_copyFileBinId(
    "FILE-UPLOAD-ID",
    "/opt/jquiver/export"
);
```

The wrapper delegates to the Java method named `jq_copyFileBinId`.

---

# 5. Database Utilities

## `jq_getDBResult`

Executes a query and returns the result rows.

```javascript
jq_getDBResult(a_strQuery, a_strdataSourceID, a_requestParams)
```

### Parameters

| Parameter | Description |
|---|---|
| `a_strQuery` | SQL query using named parameters |
| `a_strdataSourceID` | Datasource identifier; a null or empty value may use the default datasource |
| `a_requestParams` | Map containing named SQL parameter values |

### Typical response

```javascript
{
    actionStatus: "true",
    data_list: [...]
}
```

### Example

```javascript
var params = {
    status: "ACTIVE"
};

var result = jq_getDBResult(
    "SELECT id, name FROM application_user WHERE status = :status",
    null,
    params
);

if (result.actionStatus == "true") {
    var rows = result.data_list;
}
```

> Always use named parameters. Never concatenate untrusted values into SQL.

---

## `jq_callStoredProcedure`

Executes a stored procedure.

```javascript
jq_callStoredProcedure(a_strQuery, a_strdataSourceID, a_requestParams)
```

Although the wrapper parameter is named `a_strQuery`, the Java implementation treats it as the stored-procedure name.

### Parameters

| Parameter | Description |
|---|---|
| `a_strQuery` | Stored-procedure name |
| `a_strdataSourceID` | Datasource identifier |
| `a_requestParams` | Stored-procedure input parameter map |

### Example

```javascript
var params = {
    applicationId: "APP-1001"
};

var result = jq_callStoredProcedure(
    "process_application",
    null,
    params
);
```

The implementation returns the first result set under `data_list` when available.

---

## `jq_updateDBQuery`

Executes an insert, update, or delete statement.

```javascript
jq_updateDBQuery(a_strQuery, a_strdataSourceID, a_requestParams)
```

### Typical response

```javascript
{
    actionStatus: "true",
    affectedRowCount: "1"
}
```

### Example

```javascript
var params = {
    id: "APP-1001",
    status: "APPROVED"
};

var result = jq_updateDBQuery(
    "UPDATE application SET status = :status WHERE id = :id",
    null,
    params
);
```

> Use parameterised queries and restrict database operations according to application permissions.

---

# 6. REST Execution

## `jq_executeRESTCall`

Executes a JQuiver REST XML definition.

```javascript
jq_executeRESTCall(a_strRestXML)
```

### Parameters

| Parameter | Description |
|---|---|
| `a_strRestXML` | REST XML definition or evaluated REST XML content |

For internal API calls, the Java implementation notes that the URL should use the `JAPI` prefix rather than `API`.

### Example

```javascript
var response = jq_executeRESTCall(restXml);
```

The method returns the underlying JQuiver custom response object.

---

# 7. Mail Utilities

## `jq_sendMail`

Executes a JQuiver mail XML definition without an additional parameter map.

```javascript
jq_sendMail(a_strMailXML)
```

### Example

```javascript
var response = jq_sendMail(mailXml);
```

---

## `jq_sendMailWithMap`

Executes a JQuiver mail XML definition with a request-parameter map.

```javascript
jq_sendMailWithMap(a_strMailXML, a_requestParams)
```

### Example

```javascript
var params = {
    recipientName: "User",
    ticketNumber: "TKT-1001"
};

var response = jq_sendMailWithMap(mailXml, params);
```

The Java implementation delegates to the same overloaded `sendMail` method used by `jq_sendMail`.

---

# 8. Template Utilities

## `jq_evalTemplateByName`

Loads a registered template by name and evaluates it using the supplied context.

```javascript
jq_evalTemplateByName(a_strTemplateName, a_requestParams)
```

### Example

```javascript
var context = {
    userName: "Aniruddha",
    ticketNumber: "TKT-1001"
};

var output = jq_evalTemplateByName(
    "ticket-acknowledgement",
    context
);
```

If processing fails outside a custom stop condition, the current Java implementation logs the error and returns the supplied template name.

---

## `jq_evalTemplateByContent`

Evaluates supplied template content using the supplied context.

```javascript
jq_evalTemplateByContent(a_strTemplateContent, a_requestParams)
```

### Example

```javascript
var output = jq_evalTemplateByContent(
    "Hello ${userName}",
    { userName: "Aniruddha" }
);
```

If processing fails outside a custom stop condition, the current Java implementation logs the error and returns the original template content.

---

# 9. PDF Utilities

## `jq_convertToPDFFromTemplate`

Evaluates a named template and converts the resulting content to PDF.

```javascript
jq_convertToPDFFromTemplate(
    a_strTemplateName,
    a_contextValues,
    a_strImageFolder
)
```

### Parameters

| Parameter | Description |
|---|---|
| `a_strTemplateName` | Registered template name |
| `a_contextValues` | Template context map |
| `a_strImageFolder` | Optional base folder or URI used to resolve images |

### Example

```javascript
var result = jq_convertToPDFFromTemplate(
    "invoice-template",
    { invoiceNumber: "INV-1001" },
    "/opt/jquiver/images"
);
```

---

## `jq_convertToPDFFromString`

Converts supplied HTML or supported source content to PDF.

```javascript
jq_convertToPDFFromString(a_strSourceBody, a_strImageFolder)
```

### Example

```javascript
var result = jq_convertToPDFFromString(
    "<html><body><h1>Invoice</h1></body></html>",
    "/opt/jquiver/images"
);
```

The returned map should be checked for `actionStatus` and file-related result information.

---

# 10. Activity, Notification, and Error Utilities

## `jq_logActivity`

Records application activity using the JQuiver activity-log service.

```javascript
jq_logActivity(a_requestParams)
```

### Parameters

| Parameter | Description |
|---|---|
| `a_requestParams` | Map containing the fields expected by the activity-log configuration |

### Example

```javascript
jq_logActivity({
    action: "APPLICATION_APPROVED",
    entityId: "APP-1001"
});
```

The exact required map fields depend on the configured activity-log implementation.

---

## `jq_addNotifications`

Adds a notification using the JQuiver notification service.

```javascript
jq_addNotifications(a_requestParams)
```

### Example

```javascript
var result = jq_addNotifications({
    title: "Application approved",
    message: "Application APP-1001 has been approved."
});
```

The wrapper delegates to the Java method `addNotification`.

The exact required fields depend on the notification implementation and configuration.

---

## `jq_sendError`

Stops processing with a custom HTTP status code and message.

```javascript
jq_sendError(a_statuscode, a_message)
```

### Parameters

| Parameter | Description |
|---|---|
| `a_statuscode` | HTTP status code |
| `a_message` | Error message or message key |

### Example

```javascript
if (application == null) {
    jq_sendError(404, "Application not found");
}
```

Current Java defaults:

- Status code: `400`, when null or zero
- Message: `jws.defaultErrorMessage`, when blank

The method throws a custom stop exception and does not return normally.

---

# 11. Path Variable Utilities

## `jq_getPathVariableSet`

Returns the current path-variable set.

```javascript
jq_getPathVariableSet()
```

### Example

```javascript
var pathVariables = jq_getPathVariableSet();
```

---

## `jq_getPathVariable`

Returns the current path-variable value or object exposed by the execution context.

```javascript
jq_getPathVariable()
```

### Example

```javascript
var pathVariable = jq_getPathVariable();
```

The exact returned structure depends on the current Dynamic REST execution context.

---

# 12. Complete Method Index

| Category | Method |
|---|---|
| System | `jq_getSystemProperty(a_propertyName)` |
| System | `jq_getSystemEnvironment(a_propertyName)` |
| Cookie | `jq_updateCookies(a_strKey, a_strValue)` |
| Cookie | `jq_updateCookies(a_strKey, a_strValue, maxAge)` |
| Cookie | `jq_updateCookies(a_strKey, a_strValue, httpOnly)` |
| Cookie | `jq_updateCookies(a_strKey, a_strValue, maxAge, httpOnly)` |
| Cookie | `jq_updateCookieSecurity(a_strKey, isSecured)` |
| Cookie | `jq_getCookiesFromRequest(a_strKey)` |
| Cookie | `jq_haveCookie(a_strKey)` |
| Cookie | `jq_deleteCookie(a_strKey)` |
| Session-compatible | `jq_updateSession(a_strKey, a_strValue)` |
| Session-compatible | `jq_getValueFromSession(a_strKey)` |
| Session-compatible | `jq_haveSessionKey(a_strKey)` |
| Session-compatible | `jq_deleteSessionKey(a_strKey)` |
| Inactive | `jq_getCreationTime()` |
| Inactive | `jq_getLastAccessedTime()` |
| File | `jq_getAllFiles(a_filePath)` |
| File | `jq_deleteFile(a_filePath)` |
| File | `jq_saveFile(a_strFileContent, a_strTargetFileName)` |
| File bin | `jq_saveFileFromPath(a_strFilePath, a_strFileBinID, a_strcontextID)` |
| File bin | `jq_saveFileBin(a_strFileContent, a_strTargetFileName, a_strFileBinID, a_strcontextID)` |
| File | `jq_getFileContent(a_strAbsolutePath)` |
| File bin | `jq_getFileBinContent(a_strfileUploadID)` |
| File | `jq_copyFile(sourceFilePath, destinationFilePath)` |
| File | `jq_copyFile(sourceFilePath, destinationFilePath, a_strTargetFileName)` |
| File bin | `jq_copyFileBinId(a_strfileUploadID, destinationFilePath)` |
| Database | `jq_getDBResult(a_strQuery, a_strdataSourceID, a_requestParams)` |
| Database | `jq_callStoredProcedure(a_strQuery, a_strdataSourceID, a_requestParams)` |
| Database | `jq_updateDBQuery(a_strQuery, a_strdataSourceID, a_requestParams)` |
| REST | `jq_executeRESTCall(a_strRestXML)` |
| Mail | `jq_sendMail(a_strMailXML)` |
| Mail | `jq_sendMailWithMap(a_strMailXML, a_requestParams)` |
| Template | `jq_evalTemplateByName(a_strTemplateName, a_requestParams)` |
| Template | `jq_evalTemplateByContent(a_strTemplateContent, a_requestParams)` |
| PDF | `jq_convertToPDFFromTemplate(a_strTemplateName, a_contextValues, a_strImageFolder)` |
| PDF | `jq_convertToPDFFromString(a_strSourceBody, a_strImageFolder)` |
| Activity | `jq_logActivity(a_requestParams)` |
| Notification | `jq_addNotifications(a_requestParams)` |
| Error | `jq_sendError(a_statuscode, a_message)` |
| Path variable | `jq_getPathVariableSet()` |
| Path variable | `jq_getPathVariable()` |

---

# 13. Security Guidance

- Do not concatenate user input into SQL.
- Do not expose system properties or environment variables containing secrets.
- Do not pass untrusted file paths to file-system methods.
- Validate filenames and prevent path traversal.
- Restrict file-bin access by application role and business context.
- Treat cookie values as client-controlled input.
- Use secure and HTTP-only cookies for sensitive values.
- Avoid storing confidential information through the cookie-backed session-compatible methods.
- Validate REST and mail XML content before execution.
- Do not expose raw exception stack traces to end users.
- Use `jq_sendError` with user-safe messages.
- Apply authorization before executing database, file, mail, or notification operations.

---