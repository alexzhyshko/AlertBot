FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/alexzhyshko/AlertBot.git

FROM maven:3.5-jdk-8-alpine
WORKDIR /app
COPY --from=0 /app/AlertBot /app

FROM openjdk:8-jre-alpine
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java
ENV DB_USER root
ENV DB_PASSWORD root
ENV DB_CONNECTION_URL jdbc:mysql://localhost:3306/alertbot?serverTimezone=GMT%2B3&useAffectedRows=true&useUnicode=true&serverEncoding=utf8&autoReconnect=true
ENV MYSQL_CONNECTION_URL jdbc:mysql://localhost:3306/
WORKDIR /app
COPY --from=1 /app/target/AlertBot-0.0.1-SNAPSHOT-shaded.jar /app

CMD java -jar AlertBot-0.0.1-SNAPSHOT-shaded.jar
