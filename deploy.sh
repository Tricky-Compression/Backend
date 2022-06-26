#!/usr/bin/env bash

TAG="apozdniakov/tricky-compression:1.0.0"

#./gradlew assemble --quiet --build-cache --parallel
sudo docker build -f Dockerfile-run-only -t $TAG .
sudo docker push $TAG
