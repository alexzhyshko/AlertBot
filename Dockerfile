FROM alpine/git as downloadSourceCode
WORKDIR /app
RUN git clone https://github.com/mixaverros88/docker-with-java-and-mysql

FROM maven:3.5-jdk-8-alpine as packageSourceCode
WORKDIR /app
COPY --from=downloadSourceCode /app/docker-with-java-and-mysql/java /app
RUN mvn clean package

FROM openjdk:8

ENV DB_USER root
ENV DB_PASSWORD root
ENV DB_CONNECTION_URL jdbc:mysql://localhost:3306/alertbot?serverTimezone=GMT%2B3&useAffectedRows=true&useUnicode=true&serverEncoding=utf8&autoReconnect=true
ENV MYSQL_CONNECTION_URL jdbc:mysql://localhost:3306/

COPY --from=packageSourceCode /app/target/AlertBot-0.0.1-SNAPSHOT-shaded.jar /usr/src/myapp
WORKDIR /usr/src/myapp
CMD ["java -jar", "AlertBot-0.0.1-SNAPSHOT-shaded.jar"]
