FROM eclipse-temurin:17-jre

RUN mkdir -p /usr/local/socks

WORKDIR /usr/local/socks

COPY ./target/socks-rest-0.0.2-SNAPSHOT.jar /usr/local/socks/socks-rest.jar

ENTRYPOINT ["java", "-jar", "./socks-rest.jar"]