#!/usr/bin/env sh

docker rm border-state-bot-app

docker buildx create --use

docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t menkomihail/border-state-bot-app:v1.0.1 \
  -f app/Dockerfile \
  ../ \
  --push