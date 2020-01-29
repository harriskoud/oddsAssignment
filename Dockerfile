FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/*.jar
COPY ${JAR_FILE} oddschecker-0.0.1.jar
ENTRYPOINT ["java","-jar","/oddschecker-0.0.1.jar"]




