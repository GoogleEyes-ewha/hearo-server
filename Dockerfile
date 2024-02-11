FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/hearo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app.jar
COPY src/main/resources/keystore.p12 keystore.p12

ENV GOOGLE_APPLICATION_CREDENTIALS=src/main/resources/credentials.json /app/credentials.json

ENTRYPOINT ["java","-jar","/app.jar"]
