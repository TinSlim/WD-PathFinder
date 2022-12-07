# Compila CSS y JS
FROM node:16.17.1 as NODE

WORKDIR /app/frontend
ENV PATH /app/frontend/node_modules/.bin:$PATH
COPY frontend ./

RUN npm i
RUN npm run prod-css
RUN npm run prod-js


# Crea .jar
FROM maven:3.8.4-jdk-11 as MAVEN

COPY rdf-entity-path /app/rdf-entity-path
WORKDIR /app/rdf-entity-path
RUN mvn package


# Inicia Servidor del .jar
FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=rdf-entity-path-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY --from=maven /app/rdf-entity-path/target/${JAR_FILE} /opt/app/
ENTRYPOINT ["java","-jar","rdf-entity-path-0.0.1-SNAPSHOT.jar"]
