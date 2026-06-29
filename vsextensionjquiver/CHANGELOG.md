# Change Log

All notable changes to the **JQuiver VS Code Extension** will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/).

---

## [0.0.4] - 2026-06-29

### Added
- Added a **Compare with Server** feature to display differences between the locally edited JQuiver resource and the latest server version using the VS Code diff editor.
- Added support to fetch the latest server-side content for a JQuiver resource, enabling users to review the most recent version before resolving save conflicts.

### Improved
- Enhanced the conflict resolution workflow by providing a side-by-side comparison of local and server content.
- Improved retrieval of server-side resources by introducing a dedicated utility for fetching the latest content.
- Refactored the extension codebase by partitioning functionality into dedicated modules and utility files, improving code organization, readability, and maintainability.
- Modularized conflict handling and server communication logic to encourage code reuse and simplify future enhancements.

### Fixed
- Improved save conflict handling by ensuring users can accurately compare local and server versions before proceeding with further actions.

---

## [0.0.3] - 2026-06-18

### Added
- Added a **Fetch Latest** option while saving a JQuiver resource when the server indicates that the resource has been modified remotely.

### Improved
- Improved save conflict handling by allowing users to fetch the latest server-side content directly from the warning message.
- Extracted the latest-content reload logic into a reusable `fetchLatestContent` method for better maintainability.
- Updated the save flow to identify the currently opened editor and reload the corresponding JQuiver resource from the server when the user selects **Fetch Latest**.

### Fixed
- Prevented users from unknowingly overwriting server-side changes when saving a locally edited JQuiver resource.

---

## [0.0.2] - 2026-06-18

### Added
- Added a **Reload JQuiver Workspace** button in the JQuiver footer to allow users to quickly refresh the JQuiver workspace.

### Improved
- Added validation to verify the presence of the `config.jquiver` file in the selected workspace and provide appropriate feedback when it is missing.
- Updated the file state handling logic to display files in the **Modified** state only when actual changes are detected, reducing unnecessary change indicators.

### Fixed
- Improved workspace refresh behavior and overall extension responsiveness.

---

## [0.0.1] - 2026-06-15

### Added
- Initial public release of the JQuiver VS Code Extension.