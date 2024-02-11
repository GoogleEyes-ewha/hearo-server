FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/hearo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
COPY src/main/resources/keystore.p12 keystore.p12
COPY src/main/resources/credentials.json /app/credentials.json

ENV GOOGLE_APPLICATION_CREDENTIALS=/app/credentials.json

ENTRYPOINT ["java","-jar","/app.jar"]
