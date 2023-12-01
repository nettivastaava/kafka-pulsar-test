FROM valkama.saunalahti.fi/image/openjdk:21-alpine-jre-hotspot

COPY .env .
COPY build/libs/pulsar-node-1.0-SNAPSHOT-all.jar /app/pulsar-node.jar
EXPOSE 6650

ENTRYPOINT ["java", "-jar", "/app/pulsar-node.jar"]