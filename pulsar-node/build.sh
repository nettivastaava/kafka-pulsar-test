# /bin/sh

gradle build
docker build -t pulsar-node:latest .
