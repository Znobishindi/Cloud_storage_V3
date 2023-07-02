FROM openjdk:17
VOLUME /tmp
EXPOSE 8090
ADD target/cloud_storage_v3-0.0.1-SNAPSHOT.jar app-cloud.jar
ENTRYPOINT ["java", "-jar", "/app-cloud.jar"]