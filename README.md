# JQuiver

Objective of this project to rapdily develop **Enterprise Web Application**. Ideally in normal situation anyone with basic knowledge of HTML, JavaScript, JQuery, CSS & SQL (MariaDB / MySQL / PostgreSQL) will be able to work on the project. So people from different development segment will be  able to work together.  

Also the development is live development. Developers can work right on the SIT system. While the manager can monitor the development live.

☆ Type Ahead / Auto Complete<br>
☆ Grid / Tabular Data (using PQGrid)<br>
☆ Template (using FreeMarker)<br>
☆ Form Builder<br>
☆ REST API Builder<br>
☆ ⇨ Scheduler<br>
☆ ⇨ Security (RSA / AES etc.)<br>
☆ File Manager<br>
☆ Site Layout<br>
☆ Mail Configuration<br>
☆ Export / Import<br>

**Technology stack recommended**

|Tool|Version|Download Link|
|---|----|----|
|Java|Oracle 11|https://www.oracle.com/in/java/technologies/javase/jdk11-archive-downloads.html|
|MariaDB|10.5|https://mariadb.com/resources/blog/installing-mariadb-10-on-centos-7-rhel-7/|
|Maven|Latest|https://maven.apache.org/download.cgi|
|Git Client|Latest|https://git-scm.com/downloads|
|SQLYog|Community|https://github.com/webyog/sqlyog-community/wiki/Downloads|

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

<b>3)</b> The default setting is to run the application from jar, like any standard springboot application. But in case you want to run your application from any container like Tomcat or so,  then locate _pom.xml_ and change the configuration.

<b>4)</b> Rest run mvn clean install and then run as per your configuration.
```
mvn -DskipTests clean install
```
