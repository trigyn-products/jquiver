# Change Log

All notable changes to the "jquiver-extension" extension will be documented in this file.

Check [Keep a Changelog](http://keepachangelog.com/) for recommendations on how to structure this file.

## [0.0.1] - 2026-06-15

- Initial release


## [0.0.2] - 2026-06-18

### Added
- Added a **Reload JQuiver Workspace** button in the JQuiver footer to allow users to quickly refresh the JQuiver workspace.

### Improved
- Added validation to verify the presence of the `config.jquiver` file in the selected workspace and provide appropriate feedback when it is missing.
- Updated the file state handling logic to display files in the **Modified** state only when actual changes are detected, reducing unnecessary change indicators.

### Fixed
- Improved workspace refresh behavior and overall extension responsiveness.