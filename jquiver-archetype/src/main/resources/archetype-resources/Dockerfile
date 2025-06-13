## This file contains the details of application and its deendency, which needs to be executed

## Below is the detail of java version, which needs to be referred
FROM openjdk:22

## Details of target application jar
COPY target/${application jar name}.jar app.jar
COPY target/application.yml application.yml
COPY target/logback.xml logback.xml

## Below is the port where application is running
EXPOSE ${APPLICATION_PORT}
## This is the instruction/primary command to execute, when the container starts
ENTRYPOINT ["java", "-jar", "/app.jar"]

## Uncomment the below entry point if you want external application.yml
#ENTRYPOINT ["java", "-Dspring.config.location=file:/application.yml", "-jar", "/app.jar"]

## Uncomment the below entry point if you want external application.yml or external logback.xml
#ENTRYPOINT ["java", "-Dspring.config.location=file:/application.yml", "-Dlogging.config=file:/logback.xml", "-jar", "/app.jar"]
