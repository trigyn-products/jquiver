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

1. How can I run JQuiver application, using a jar?
	java -jar <application-jar-file.jar> . This command will take up the application.yml file and logback.xml file already available in the jar.
	
2. How can I run JQuiver application jar, if I have an external yml file?
	java -jar <application-jar-file.jar> --spring.config.location=<absolutepath of application.yml file>
	
3. How can I run JQuiver application jar, if I have an external logback file?
	java -jar <application-jar-file.jar> -Dlogging.config=<absolutepath of logback.xml file>
	
4. How can I run JQuiver application jar, if I have an external yml and logback file?
	java -jar <application-jar-file.jar> --spring.config.location=<absolutepath of application.yml file> -Dlogging.config=<absolutepath of logback.xml file>
	
5. How to start the application using docker?
	a. Build the application, which needs to be started.
	a. Update the Dockerfile and docker-compose.yml, available in the project.
	b. Run the below commands sequentially,
		docker-compose build  (docker compose build is also ok)
		docker-compose up     (docker compose up is also ok)
		
6. How to start the application using docker, if my application.yml file is to be referred from outside the application jar?
	a. Build the application, which needs to be started.
	a. Update the Dockerfile and docker-compose.yml, available in the project.
	b. Run the below commands sequentially,
		docker-compose build
		docker run `-v "<absolutepath- of-external-application.yml-path>:/application.yml" ` -e SPRING_CONFIG_LOCATION="file:/application.yml" ` <application-image-name-in-docker>
		
7. How to start the application using docker, if my application.yml or logback file is to be referred from outside the application jar?
	a. Build the application, which needs to be started.
	a. Update the Dockerfile and docker-compose.yml, available in the project.
	b. Run the below commands sequentially,
		docker-compose build
		docker run `-v "<absolutepath- of-external-application.yml-path>:/application.yml" ` -v "<absolutepath-of-external-logback.xml-path>:/logback.xml" ` -e SPRING_CONFIG_LOCATION="file:/application.yml" ` <application-image-name-in-docker>

8. How to configure redisson caching in JQuiver
	a. Install and start the redisson server.
	b. Update the redisson.yaml file, with the server ip address and client name.
	c. Uncomment the redisson configuration in application.yml file.
	d. Provide the redisson.yaml absolute file path, if it is an external file or mention the file name.
	e. mark the "enabled" configuration as true.
	
9. How to configure Local server DB connection instead of Docker DB
	a . In application.yml at the place of host we should define host.docker.internal:3306 like this
	b . Comment the (maria db, environment and Port) configuration inside  docker-compose.yml . Not needed when connecting to Local DB.
	c . For SMTP OTP configuration add host.docker.internal in place of localhost.	