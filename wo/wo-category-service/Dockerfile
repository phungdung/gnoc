ARG JDK_BASE_IMAGE
FROM ${JDK_BASE_IMAGE}
ARG PROJECT_VERSION
ENV PROJECT_VERSION ${PROJECT_VERSION}
WORKDIR /app
COPY target/wo-category-service-${PROJECT_VERSION}.jar wo-category-service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:///dev/urandom","-jar", "/app/wo-category-service.jar"]
CMD ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9102","--spring.profiles.active=native,dev"]
