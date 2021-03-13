ARG JDK_BASE_IMAGE
FROM ${JDK_BASE_IMAGE}
ARG PROJECT_VERSION
ENV PROJECT_VERSION ${PROJECT_VERSION}
WORKDIR /app
COPY target/kedb-service-${PROJECT_VERSION}.jar kedb-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:///dev/urandom","-jar", "/app/kedb-service.jar"]
CMD ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9069","--spring.profiles.active=native,dev"]
