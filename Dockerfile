FROM openjdk:11
ARG JAR_FILE=target/jwt-project-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} jwt-project-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar" , "jwt-project-0.0.1-SNAPSHOT.jar"]