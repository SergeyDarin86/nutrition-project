FROM openjdk:17-jdk-slim-buster
ARG JAR_FILE=target/nutrition-recommendation-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]