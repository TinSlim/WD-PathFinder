FROM openjdk:11-jdk as JDK
FROM maven:3.8.4-jdk-11 as MAVEN
RUN mvn package
EXPOSE 8080