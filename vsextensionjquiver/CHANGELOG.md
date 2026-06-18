# Change Log

All notable changes to the "jquiver-extension" extension will be documented in this file.

Check [Keep a Changelog](http://keepachangelog.com/) for recommendations on how to structure this file.

## [0.0.1] - 2026-06-15

* Initial release


## [0.0.2] - 2026-06-18

### Added
* Added a **Reload JQuiver Workspace** button in the JQuiver footer to allow users to quickly refresh the JQuiver workspace.

### Improved
* Added validation to verify the presence of the `config.jquiver` file in the selected workspace and provide appropriate feedback when it is missing.
* Updated the file state handling logic to display files in the **Modified** state only when actual changes are detected, reducing unnecessary change indicators.

### Fixed
* Improved workspace refresh behavior and overall extension responsiveness.


## [0.0.3] - 2026-06-18
 
### Added
 
* Added a Fetch Latest option while saving a JQuiver resource when the server indicates that the resource has been modified remotely.
 
### Improved
 
* Improved save conflict handling by allowing users to fetch the latest server-side content directly from the warning message.
* Extracted latest-content reload logic into a reusable `fetchLatestContent` method for better maintainability.
* Updated the save flow to identify the currently opened editor and reload the corresponding JQuiver resource from the server when the user selects Fetch Latest.
 
### Fixed
 
* Prevented users from unknowingly overwriting server-side changes when saving a locally edited JQuiver resource.