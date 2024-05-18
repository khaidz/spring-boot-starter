FROM openjdk:17-jdk
MAINTAINER khaibq
RUN mkdir -p /var/www/deploy
COPY target/spring-boot-starter.jar /var/www/deploy/spring-boot-starter.jar
WORKDIR /var/www/deploy
CMD ["java", "-jar", "spring-boot-starter.jar"]