FROM openjdk:11-jre-slim

EXPOSE 8080
ARG APP_NAME=my-spring-boot-app
ARG SPRING_PROFILES_ACTIVE=prod 
ENV APP_NAME $APP_NAME
ENV SPRING_PROFILES_ACTIVE $SPRING_PROFILES_ACTIVE
VOLUME /app
ADD target/DevOps_Project-1.0.jar /app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=$SPRING_PROFILES_ACTIVE", "/app.jar"]
