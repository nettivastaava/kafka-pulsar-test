FROM openjdk

COPY build/libs/pulsar-node-1.0-SNAPSHOT-all.jar /app/pulsar-node.jar

RUN chmod 755 /app/pulsar-node.jar

COPY src/main/resources/application.yaml /config/

ENTRYPOINT ["java", "-jar", "/app/pulsar-node.jar"]