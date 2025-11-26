# Stage 1: build WAR
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Tomcat
FROM tomcat:10.1-jdk21
RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

COPY configuration.yaml /usr/local/tomcat/

EXPOSE 8080
CMD ["catalina.sh", "run"]
