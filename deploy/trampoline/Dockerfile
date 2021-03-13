ARG IMAGE_REGISTRY_URL
FROM ${IMAGE_REGISTRY_URL}/centos:oracle-jdk-10

WORKDIR /app
COPY trampoline.jar .

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/trampoline.jar" ]
