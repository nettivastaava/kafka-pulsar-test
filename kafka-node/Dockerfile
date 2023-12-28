FROM openjdk

COPY build/libs/kafka-node-1.0-SNAPSHOT-all.jar /app/kafka-node.jar
COPY src/main/resources/application.yaml /config/

ENTRYPOINT ["java", "-jar", "/app/kafka-node.jar"]