ARG JDK_BASE_IMAGE
FROM ${JDK_BASE_IMAGE}
ARG PROJECT_VERSION
ENV PROJECT_VERSION ${PROJECT_VERSION}
WORKDIR /app
COPY target/common-stream-service-${PROJECT_VERSION}.jar common-stream-service.jar
COPY native/libMonthCore.so /usr/lib64/libMonthCore.so
COPY native/libMonthAdapter.so /usr/lib64/libMonthAdapter.so
COPY native/libDayCore.so /usr/lib64/libDayCore.so
COPY native/libDayAdapter.so /usr/lib64/libDayAdapter.so
ENTRYPOINT ["java","-Djava.security.egd=file:///dev/urandom","-jar", "/app/common-stream-service.jar"]
CMD ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:9064","--spring.profiles.active=native,dev"]
