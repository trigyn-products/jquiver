@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@@@@@@@@@@@   @@@@@@@@@@       @@@@@@@@@@@@@@@@@@@@@@@@@@@   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@@@@@@@@@@@   @@@@@@@@           @@@@@@@@@@@@@@@@@@@@@@@@@   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@@@@@@@@@@@   @@@@@@@             @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@@@@@@@@@@@   @@@@@@     @@@@@     @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@@@@@@@@@@@   @@@@@@    @@@@@@@    @@@@@@@   @@@@@   @@@@@   @@@@@   @@@@@@@   @@@@@@@@@    @@@@@@@@@   @    @@@
@@@@@@@@@@@   @@@@@    @@@@@@@@@    @@@@@@   @@@@@   @@@@@   @@@@@   @@@@@@@   @@@@@@@        @@@@@@@        @@@
@@@@@@@@@@@   @@@@@   @@@@@@@@@@@   @@@@@@   @@@@@   @@@@@   @@@@@@   @@@@@   @@@@@@@          @@@@@@        @@@
@@@@@@@@@@@   @@@@@   @@@@@@@@@@@   @@@@@@   @@@@@   @@@@@   @@@@@@   @@@@@   @@@@@@@   @@@@   @@@@@@    @@@@@@@
@@@@@@@@@@@   @@@@@   @@@@@@@@@@@   @@@@@@   @@@@@   @@@@@   @@@@@@   @@@@@   @@@@@@   @@@@@@   @@@@@   @@@@@@@@
@@@@@@@@@@@   @@@@@   @@@@@@@@@@@   @@@@@@   @@@@@   @@@@@   @@@@@@@   @@@   @@@@@@@   @@@@@@   @@@@@   @@@@@@@@
@@@@@@@@@@@   @@@@@   @@@@@@@@@@@   @@@@@@   @@@@@   @@@@@   @@@@@@@   @@@   @@@@@@@            @@@@@   @@@@@@@@
@@@@@@@@@@@   @@@@@   @@@@@@@@@@@   @@@@@@   @@@@@   @@@@@   @@@@@@@@   @   @@@@@@@@            @@@@@   @@@@@@@@
@@   @@@@@@   @@@@@    @@@@@ @@@   @@@@@@@   @@@@@   @@@@@   @@@@@@@@   @   @@@@@@@@   @@@@@@@@@@@@@@   @@@@@@@@
@@   @@@@@@   @@@@@@    @@@        @@@@@@@   @@@@@   @@@@@   @@@@@@@@       @@@@@@@@   @@@@@@@@@@@@@@   @@@@@@@@
@@    @@@@    @@@@@@     @@@      @@@@@@@@    @@@    @@@@@   @@@@@@@@@     @@@@@@@@@@   @@@@@  @@@@@@   @@@@@@@@
@@@          @@@@@@@@              @@@@@@@           @@@@@   @@@@@@@@@     @@@@@@@@@@           @@@@@   @@@@@@@@
@@@@        @@@@@@@@@@               @@@@@@          @@@@@   @@@@@@@@@@   @@@@@@@@@@@@         @@@@@@   @@@@@@@@
@@@@@      @@@@@@@@@@@@@       @    @@@@@@@@    @@   @@@@@   @@@@@@@@@@   @@@@@@@@@@@@@      @@@@@@@@   @@@@@@@@
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


# This file includes basic information and command required to run the JQuiver platform #

1. How to setup and start the application in local environment, once it has been downloaded from maven repository?
    a. Locate the application.yml file in your project and change database connection details as per your installation and project. Remember this is your core DB, you can add additional datasources to connect to other system via databases.
    b. Update rest of the configuration in _application.yml as per the comments in the file, to support different settings. Also make sure all the placeholders marked with ${} is properly updated.
    c. The default setting is to run the application from jar, like any standard springboot application. But in case you want to run your application from any container like Tomcat or so, then locate pom.xml and change the configuration.
    d. Build the application using maven, which will create a jar file in target folder. You can also use the command "mvn clean install -DskipTests" to build the application without running the tests.
    e. Run the application using the command "java -jar <application-jar-file.jar>". This will start the application and you can access it on localhost:<port> or as per your configuration.
    
2. How can I run JQuiver application, using a jar?
	java -jar <application-jar-file.jar> . This command will take up the application.yml file and logback.xml file already available in the jar.
	
3. How can I run JQuiver application jar, if I have an external yml file?
	java -jar <application-jar-file.jar> --spring.config.location=<absolutepath of application.yml file>
	
4. How can I run JQuiver application jar, if I have an external logback file?
	java -jar <application-jar-file.jar> -Dlogging.config=<absolutepath of logback.xml file>
	
5. How can I run JQuiver application jar, if I have an external yml and logback file?
	java -jar <application-jar-file.jar> --spring.config.location=<absolutepath of application.yml file> -Dlogging.config=<absolutepath of logback.xml file>
	
6. How to start the application using docker?
	a. Build the application, which needs to be started.
	a. Update the Dockerfile and docker-compose.yml, available in the project.
	b. Run the below commands sequentially,
		docker-compose build  (docker compose build is also ok)
		docker-compose up     (docker compose up is also ok)
		
7. How to start the application using docker, if my application.yml file is to be referred from outside the application jar?
	a. Build the application, which needs to be started.
	a. Update the Dockerfile and docker-compose.yml, available in the project.
	b. Run the below commands sequentially,
		docker-compose build
		docker run `-v "<absolutepath- of-external-application.yml-path>:/application.yml" ` -e SPRING_CONFIG_LOCATION="file:/application.yml" ` <application-image-name-in-docker>
		
8. How to start the application using docker, if my application.yml or logback file is to be referred from outside the application jar?
	a. Build the application, which needs to be started.
	a. Update the Dockerfile and docker-compose.yml, available in the project.
	b. Run the below commands sequentially,
		docker-compose build
		docker run `-v "<absolutepath- of-external-application.yml-path>:/application.yml" ` -v "<absolutepath-of-external-logback.xml-path>:/logback.xml" ` -e SPRING_CONFIG_LOCATION="file:/application.yml" ` <application-image-name-in-docker>

9. How to configure redisson caching in JQuiver
	a. Install and start the redisson server.
	b. Update the redisson.yaml file, with the server ip address and client name.
	c. Uncomment the redisson configuration in application.yml file.
	d. Provide the redisson.yaml absolute file path, if it is an external file or mention the file name.
	e. mark the "enabled" configuration as true.
	
10. How to configure Local server DB connection instead of Docker DB
	a . In application.yml at the place of host we should define host.docker.internal:3306 like this
	b . Comment the (maria db, environment and Port) configuration inside  docker-compose.yml . Not needed when connecting to Local DB.
	c . For SMTP OTP configuration add host.docker.internal in place of localhost.	
	
11. What is the default username and password for JQuiver application, when database authentication is enabled?
	The default username and password for JQuiver application, when database authentication is enabled, is as follows:
	username: admin@localhost.io
	password: Account@123
	
12. How to update the logger level for JQuiver application?
	Logging is an important aspect of any application, as it helps in monitoring and debugging. In JQuiver application, the logging is configured using logback framework.    To update the logger level for JQuiver application, you can follow these steps:    a. Locate the logback.xml file at src/main/resources.    b. Update the logger level as per your requirement. You can set the level to TRACE, DEBUG, INFO, WARN, or ERROR.    c. Save the changes and restart the application for the changes to take effect.
    d. To update the logger level of flyway, update the property "logging.level.org.flywaydb" in application.yml file. The default level is INFO, but you can change it to DEBUG or TRACE for more detailed logging.