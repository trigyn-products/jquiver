# JQuiver VS Code Extension

JQuiver is a low-code development framework designed to simplify and accelerate enterprise application development. It enables developers to build applications faster using reusable components, configuration-driven screens, dynamic REST APIs, forms, grids, dashboards, dashlets, templates, and other development artifacts.

The **JQuiver VS Code Extension** allows developers to work with JQuiver application artifacts locally from Visual Studio Code and save changes back to the remote JQuiver server.

---

## Getting Started

To use this extension, first download the VS Code configuration file from your JQuiver application.

Navigate to:

**Control Panel → Application Configuration**

Click the **Download VS Config** button. This will download a file named:

```text
config.jquiver
```

Create an empty folder on your local machine and save the **config.jquiver** file inside that folder.

After installing the JQuiver VS Code Extension, open this folder in Visual Studio Code.

Once the folder is opened, the extension will read the configuration file and reload all editable JQuiver artifacts, including:

- Templates
- Forms
- Dashboards
- Dashlets
- Dynamic REST APIs
- Other supported editable resources

Developers can then work with these artifacts locally and save the updates back to the configured remote JQuiver server.

---

## Important Configuration Note

If user authentication is not enabled when the **config.jquiver** file is downloaded, and user authentication is enabled later in the JQuiver application, then the **config.jquiver** file must be downloaded again from:

**Control Panel → Application Configuration → Download VS Config**

This ensures that the extension has the latest authentication-related configuration required to connect with the JQuiver server.

---

## Auto Refresh Configuration

The extension supports automatic refresh of JQuiver resources.

The reload frequency can be changed by updating the following property in the **config.jquiver** file:

```json
{
  "autoRefreshInMinutes": 5
}
```

### Allowed Range

```text
1 to 30 minutes
```

### Default Value

```text
5 minutes
```

If the value is not provided or is outside the allowed range, the extension will automatically use the default refresh interval.

---

## Key Features

### Resource Synchronization

- Automatically loads editable JQuiver resources from the server.
- Supports local editing of application artifacts.
- Saves changes directly back to the remote JQuiver server.

### Supported Artifacts

- Templates
- Forms
- Dashboards
- Dashlets
- Dynamic REST APIs
- Other editable JQuiver resources

### Developer Friendly

- Native Visual Studio Code integration.
- Simplified development workflow.
- Automatic resource refresh.
- Centralized configuration through `config.jquiver`.

---

## Prerequisites

Before using the extension, ensure that:

- You have access to a JQuiver application instance.
- You have Visual Studio Code installed.
- You have downloaded the latest `config.jquiver` file from the JQuiver application.

---

## Workflow

1. Install the JQuiver VS Code Extension.
2. Download the `config.jquiver` file from:

   **Control Panel → Application Configuration → Download VS Config**

3. Create a local workspace folder.
4. Place the `config.jquiver` file inside the folder.
5. Open the folder in Visual Studio Code.
6. Allow the extension to load JQuiver resources.
7. Edit resources locally.
8. Save changes back to the JQuiver server.

---

## Notes

- Always download a fresh `config.jquiver` file after enabling authentication in JQuiver.
- Keep the configuration file updated whenever connection settings change.
- Auto-refresh helps keep local resources synchronized with the server.
- Invalid auto-refresh values automatically fall back to the default interval of 5 minutes.

---

## About JQuiver

JQuiver is an enterprise-grade low-code application development platform that enables rapid creation of business applications using configuration-driven development, reusable components, forms, dashboards, workflows, APIs, and templates.

The VS Code Extension extends the JQuiver development experience by allowing developers to work with application artifacts directly from Visual Studio Code while maintaining seamless synchronization with the JQuiver platform.