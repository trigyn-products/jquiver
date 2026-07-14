# JQuiver Mail Server Listener Plugin

The **Mail Server Listener Plugin** enables a JQuiver application to monitor an email account and process incoming messages automatically.

The listener connects to a configured mail server, reads incoming emails, captures message and attachment information, and invokes a configured JQuiver REST API or application endpoint for further processing.

Typical use cases include:

- Creating support tickets from incoming emails
- Processing email-based requests
- Capturing email attachments
- Triggering workflows from received messages
- Integrating shared mailboxes with JQuiver applications
- Automating document or request intake

## Features

- Configurable mail-server connection
- Support for mail protocols configured by the application
- Automatic monitoring of incoming messages
- Configurable listener name and status
- Email content extraction
- Attachment information extraction
- REST API invocation after processing an email
- Configurable REST endpoint and HTTP method
- Tracking of the listener's last execution time
- Database-driven listener configuration
- Ability to enable or disable individual listeners

## Package Contents

| File | Description |
|---|---|
| [`Mail_Server_Listner.zip`](./Mail_Server_Listner.zip) | JQuiver plugin package to be imported or deployed |
| [`sql.txt`](./sql.txt) | Database script for creating the mail-listener configuration table |

> **Note:** The spelling `Listner` in the ZIP filename is retained for compatibility with the existing package.

## How It Works

The plugin follows the general processing flow below:

```text
Mail Server
     |
     | IMAP/POP3 or configured protocol
     v
Mail Server Listener
     |
     | Read email and attachment information
     v
Configured JQuiver REST API
     |
     v
Application processing or workflow
```

For every active listener configuration, the plugin:

1. Reads the mail-server configuration.
2. Connects to the configured mailbox.
3. Checks for messages that need to be processed.
4. Extracts the relevant email information.
5. Captures attachment information, where applicable.
6. Invokes the configured REST API.
7. Updates the listener's last execution timestamp.

## Prerequisites

Before installing the plugin, ensure that the following are available:

- A running JQuiver application
- Access to the JQuiver application database
- A supported mail server
- Mail-server hostname and port
- Mailbox username and password or authentication credentials
- IMAP, POP3, or the required mail protocol enabled on the server
- Network access from the JQuiver server to the mail server
- A JQuiver REST API or application endpoint to process the received email
- Appropriate database and application-administration permissions

## Installation

### 1. Create the Database Table

Execute the script provided in:

```text
sql.txt
```

Run the script against the same database used by the JQuiver application.

The script creates the following table:

```text
msl_listener_info
```

Confirm that the table has been created successfully before importing or activating the plugin.

### 2. Import the Plugin

Import or deploy the following package using the standard JQuiver plugin-import process:

```text
Mail_Server_Listner.zip
```

Depending on the JQuiver deployment model, an application restart may be required after importing the plugin.

### 3. Configure a Listener

Create a listener configuration in the `msl_listener_info` table or through the corresponding JQuiver configuration screen provided by the imported plugin.

## Configuration Fields

| Field | Description |
|---|---|
| `mail_server_id` | Unique identifier for the mail-server configuration |
| `listener_name` | Human-readable name assigned to the listener |
| `mail_info` | Mail-server connection and mailbox configuration |
| `protocol_type` | Mail protocol used by the listener |
| `files_info` | Configuration related to attachments or extracted files |
| `restApi_id` | Identifier of the JQuiver REST API to be invoked |
| `rest_api_url` | URL of the REST endpoint |
| `rest_api_method_name` | HTTP method or configured method used for the REST invocation |
| `isActive` | Indicates whether the listener is active |
| `last_modified` | Timestamp of the most recent configuration change |
| `last_executed` | Timestamp of the most recent listener execution |

The exact format of `mail_info` and `files_info` should follow the structure expected by the plugin version being installed.

## Example Listener Configuration

A listener configuration should provide the following logical information:

```text
Listener Name      : Support Mailbox Listener
Protocol           : IMAP
Mail Server        : mail.example.com
Mail Server Port   : 993
Mailbox User       : support@example.com
SSL/TLS Enabled    : Yes
REST API           : Create Support Ticket
REST API URL       : <JQUIVER_BASE_URL>/api/<configured-endpoint>
REST Method        : POST
Active             : Yes
```

Do not store sample, default, or unencrypted production passwords in the repository.

## REST API Processing

The configured REST API should be able to accept the email information generated by the listener.

Depending on the plugin configuration, the submitted information may include:

- Sender address
- Recipient addresses
- CC addresses
- Email subject
- Email body
- Message date
- Message identifier
- Attachment names
- Attachment metadata
- Extracted file information

The target API should validate all received data before starting application processing.

A typical API may use the received information to:

- Create a new record
- Create a support ticket
- Start a workflow
- Store an attachment
- Send an acknowledgement
- Update an existing request
- Route the message to a department or queue

## Mail-Server Requirements

Confirm the following mail-server settings before enabling the listener:

- The required protocol is enabled.
- The configured port is accessible.
- Firewall rules allow connectivity from the JQuiver server.
- SSL/TLS certificates are valid and trusted.
- The mailbox has sufficient permissions.
- The authentication mechanism is supported.
- The mailbox is not blocked by conditional-access restrictions.
- The mail server permits automated mailbox access.

For Microsoft 365, Gmail, or other providers using modern authentication, additional authentication configuration may be required.

## Security Recommendations

- Use SSL/TLS for all mail-server connections.
- Use HTTPS for REST API calls.
- Avoid storing passwords in source control.
- Use application secrets, encrypted configuration, or a secrets manager where supported.
- Assign the mailbox only the permissions required by the listener.
- Restrict access to the listener configuration table.
- Validate attachment type and size before processing.
- Scan attachments for malware before storing or opening them.
- Validate sender information before initiating sensitive workflows.
- Protect the REST endpoint with appropriate authentication and authorization.
- Do not log mailbox passwords, tokens, or sensitive email content.

## Attachment Handling

When processing attachments:

- Enforce a maximum file-size limit.
- Allow only approved file types.
- Do not rely only on the file extension.
- Validate the MIME type and file signature.
- Rename files safely before storage.
- Prevent path-traversal characters in filenames.
- Scan uploaded files for malware.
- Store files outside publicly accessible directories unless specifically required.
- Record processing failures for operational review.

## Enabling and Disabling a Listener

The listener status is controlled through the `isActive` field.

```text
isActive = 1    Listener enabled
isActive = 0    Listener disabled
```

Disable a listener before making significant configuration changes. Re-enable it after validating the mail-server and REST API settings.

## Verification

After installation and configuration:

1. Confirm that the listener is active.
2. Send a test email to the configured mailbox.
3. Verify that the listener connects successfully.
4. Confirm that the configured REST API is invoked.
5. Check whether email data is received correctly.
6. Verify attachment processing, if applicable.
7. Confirm that `last_executed` is updated.
8. Review the application logs for warnings or errors.

## Troubleshooting

### Listener Does Not Execute

Check that:

- `isActive` is set to `1`.
- The JQuiver application is running.
- The plugin was imported successfully.
- The listener configuration is valid.
- The listener service or scheduler has started.

### Mail Server Connection Fails

Check:

- Mail-server hostname
- Port number
- Protocol
- Username and password
- SSL/TLS configuration
- Firewall and proxy rules
- Mail-server access policies
- Certificate trust settings

### REST API Is Not Invoked

Check:

- `restApi_id`
- `rest_api_url`
- `rest_api_method_name`
- REST API authentication
- Endpoint availability
- JQuiver application logs
- API request and response logs

### Attachments Are Not Processed

Check:

- Attachment configuration in `files_info`
- File-size restrictions
- Allowed file types
- Storage-directory permissions
- Available disk space
- Antivirus or security-policy restrictions

### Emails Are Processed More Than Once

Check:

- Message read or processed-state handling
- Listener execution intervals
- Multiple active listeners using the same mailbox
- Listener transaction failures
- Whether the mail server preserves the message state correctly

## Operational Recommendations

- Use a dedicated mailbox for automated processing.
- Use separate listener configurations for separate business processes.
- Monitor listener execution and REST API failures.
- Configure application-level retry handling where appropriate.
- Prevent multiple listeners from processing the same mailbox unless intentionally designed.
- Archive or move successfully processed emails.
- Establish a process for handling malformed messages.
- Maintain an error queue or exception report.
- Review mailbox storage and retention periodically.

## Database Backup

Take a backup of the JQuiver database before running the installation SQL or importing the plugin into an existing environment.

Test the installation in a development or staging environment before deploying it to production.

## Compatibility

The plugin must be deployed with a compatible JQuiver version. Confirm compatibility before importing it into an upgraded or customised JQuiver installation.

## Support

When reporting an issue, provide:

- JQuiver version
- Plugin package version
- Mail-server type
- Configured protocol
- Relevant listener configuration with passwords removed
- Application log entries
- REST API response status
- Steps required to reproduce the issue

Never include passwords, access tokens, mailbox contents, or other sensitive information in an issue report.