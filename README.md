# JQuiver

Objective of this project to rapdily develop **Enterprise Web Application**. Ideally in normal situation anyone with basic knowledge of HTML, JavaScript, JQuery, CSS & SQL (MariaDB / MySQL / PostgreSQL) will be able to work on the project. So people from different development segment will be  able to work together.  

Also the development is live development. Developers can work right on the SIT system. While the manager can monitor the development live.

* Type Ahead / Auto Complete
* Grid / Tabular Data (using PQGrid)
* Template (using FreeMarker)
* Form Builder
* REST API Builder
* File Manager
* Site Layout
* Mail Configuration
* Export / Import

To install JQuiver application use below command, assuming  you know about Basic Maven and Java.

```
mvn archetype:generate -DarchetypeGroupId=com.trigyn  -DarchetypeArtifactId=jquiver -DarchetypeVersion=1.3 -DgroupId=com.mygroup -DartifactId=myartifact
```

After that change in /src/main/resources/application.yml
Change `datasource` `URL`, `username` and `password`
