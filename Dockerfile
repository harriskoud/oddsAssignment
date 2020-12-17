FROM openjdk:8-jdk-alpine
ADD build/libs/oddschecker-0.0.1.jar /oddschecker-0.0.1.jar
ENTRYPOINT ["java","-jar","oddschecker-0.0.1.jar"]