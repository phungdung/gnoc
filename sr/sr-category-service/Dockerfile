ARG JDK_BASE_IMAGE
FROM ${JDK_BASE_IMAGE}
ARG PROJECT_VERSION
ENV PROJECT_VERSION ${PROJECT_VERSION}
WORKDIR /app
COPY target/sr-category-service-${PROJECT_VERSION}.jar sr-category-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:///dev/urandom","-jar", "/app/sr-category-service.jar"]
CMD ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9111","--spring.profiles.active=native,dev"]
