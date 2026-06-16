# JQuiver

Objective of this project to rapdily develop **Enterprise Web Application**. Ideally in normal situation anyone with basic knowledge of HTML, JavaScript, JQuery, CSS & SQL (MariaDB / MySQL / PostgreSQL) will be able to work on the project. So people from different development segment will be  able to work together.  

Also the development is live development. Developers can work right on the SIT system. While the manager can monitor the development live.

```text
☆ Master Generator
☆ Multilingual
☆ Additional Datasource
☆ File Bins
  ⇨ Custom File Bins
☆ Autocomplete
  ⇨ Single Select Autocomplete
  ⇨ Multiselect Autocomplete
  ⇨ Dependent Autocomplete
☆ Grid Utils
☆ Form IO
  ⇨ Default
  ⇨ Custom
  ⇨ Custom Pluggable
☆ Templating
  ⇨ Templates
  ⇨ Email XML
  ⇨ Web Client XML
☆ Form Builder
☆ REST API
  ⇨ Java
  ⇨ FTL
  ⇨ Python
  ⇨ Nashorn JavaScript
☆ Secured REST API
☆ Scheduler
☆ Script Library
☆ API Clients
☆ Router
  ⇨ Define Home Page
☆ User Management
  ⇨ Authentication
  ⇨ User Creation
  ⇨ Role Creation
  ⇨ Permission Management
☆ Application Configuration
  ⇨ Mail Configuration
☆ Help Manual
☆ Dashboard
  ⇨ Dashlet
☆ Notification
☆ Workflow
☆ Import / Export
☆ Business Module
☆ Tag Creation
```

**Technology stack recommended**

|Tool|Version|Download Link|
|---|----|----|
|Java|Oracle 11|https://www.oracle.com/in/java/technologies/javase/jdk11-archive-downloads.html|
|MariaDB|10.5|https://mariadb.com/resources/blog/installing-mariadb-10-on-centos-7-rhel-7/|
|Maven|Latest|https://maven.apache.org/download.cgi|

**Start JQuiver Implementation**<br>
<b>1)</b> Run below command after all required tools are successfully installed on your machine. Feel free to change the DgroupId & DartifactId as per your project
```
mvn archetype:generate -DarchetypeGroupId=com.trigyn  -DarchetypeArtifactId=jquiver -DarchetypeVersion=LATEST -DgroupId=com.mygroup -DartifactId=myartifact
```
<b>2)</b> Locate the _application.yml_ file in your project and change database connection details as per your installation and project. Remember this is your core DB, you can add additional datasources to connect to other system via databases.
```
driver-class-name: org.mariadb.jdbc.Driver
url: jdbc:mysql://localhost:3306/myapplication?createDatabaseIfNotExist=true&serverTimezone=UTC&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false
username: root
password: root
```

If you are planing to run your application from any container like Tomcat or so, then you might want to opt for jndi-name instead of direct connection to database using URL, username, password.

```
jndi-name: java:comp/env/jdbc/jndiDataSource
#    driver-class-name: org.mariadb.jdbc.Driver
#    url: jdbc:mysql://localhost:3306/myapplication?createDatabaseIfNotExist=true&serverTimezone=UTC&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false
#    username: root
#    password: root
```

<b>3)</b> Update rest of the configuration in _application.yml as per the comments in the file, to support different settings. Also make sure all the placeholders marked with ${} is properly updated.

<b>4)</b> The default setting is to run the application from jar, like any standard springboot application. But in case you want to run your application from any container like Tomcat or so,  then locate _pom.xml_ and change the configuration.

<b>5)</b> Rest run mvn clean install and then run as per your configuration.
```
mvn -DskipTests clean install
```
# Working with JQuiver VS Code Extension

## Installation

1. Install the **JQuiver VS Code Extension** from the Visual Studio Code Marketplace.

## Configuration

2. Open your JQuiver application in the browser and navigate to:

   **Control Panel → Application Configuration**

3. Click the **Download VS Config** button.

4. This will download a configuration file named:

   ```text
   config.jquiver
   ```

5. Create a new empty folder on your local machine.

6. Copy the downloaded `config.jquiver` file into the newly created folder.

## Connecting to JQuiver

7. Open Visual Studio Code.

8. Open the folder where the `config.jquiver` file has been saved.

9. Once the folder is opened, the JQuiver VS Code Extension will read the configuration file and connect to the configured JQuiver server.

10. The extension will automatically reload all editable JQuiver resources, including:

    - Templates
    - Forms
    - Dashboards
    - Dashlets
    - Dynamic REST APIs
    - Other supported editable resources

## Editing Resources

11. Developers can edit these resources locally using Visual Studio Code.

12. After making the required changes, developers can save the updated resources back to the remote JQuiver server.

## Authentication Changes

13. If user authentication is **not enabled** when the `config.jquiver` file is downloaded, and authentication is enabled later in the JQuiver application, then the configuration file must be downloaded again from:

    **Control Panel → Application Configuration → Download VS Config**

## Auto Reload Configuration

14. The auto reload frequency of the extension can be changed by updating the following property in the `config.jquiver` file:

    ```json
    {
      "autoRefreshInMinutes": 5
    }
    ```

15. The allowed range for `autoRefreshInMinutes` is **1 to 30 minutes**.

16. The default auto reload interval is **5 minutes**.

17. If the value is not provided or is outside the allowed range, the extension will use the default refresh interval.
