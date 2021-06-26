FROM openjdk:11
ARG WORKER_JAR_FILE=target/*.jar
COPY ${WORKER_JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]