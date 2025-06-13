# This file contains the details of application and its deendency, which needs to be executed

# Below is the detail of java version, which needs to be referred
FROM openjdk:22
# Details of target application jar
COPY target/${application jar name}.jar app.jar
# Below is the port where application is running
EXPOSE ${APPLICATION_PORT}
# This is the instruction/primary command to execute, when the container starts
ENTRYPOINT ["java", "-jar", "/app.jar"]