#!/usr/bin/env bash
set -o pipefail
set -e
set -u

DOCKER_REGISTRY=263649354763.dkr.ecr.eu-central-1.amazonaws.com
DOCKER_REPOSITORY=flexapi

if [ "$#" -ne 1 ]; then
cat <<EOM
Script to build, tag, and publish a Docker image from an api build

Requirements:

- AWS CLI
  > brew install awscli

- ECR Login
  > \$(aws ecr get-login --region eu-central-1)

Usage: $0 BUILD_TAG
e.g. $0 latest
EOM
exit 0;
fi

BUILD_TAG=${1:-latest}

echo "Building api wth tag $BUILD_TAG"
./mvnw clean install

PROJECT_VERSION=`mvn help:evaluate -Dexpression=project.version | grep -e '^[^\[]'`
JAR_NAME="flex-api-$PROJECT_VERSION.jar"
DOCKER_BUILD_DIR="target/docker"

mkdir -p $DOCKER_BUILD_DIR
cp ./docker/Dockerfile $DOCKER_BUILD_DIR
cp ./target/$JAR_NAME $DOCKER_BUILD_DIR/flex-api.jar
echo "Copied $JAR_NAME to $DOCKER_BUILD_DIR"

cd $DOCKER_BUILD_DIR

echo "About to build image locally $DOCKER_REPOSITORY:$BUILD_TAG"
docker build -t "${DOCKER_REPOSITORY}:${BUILD_TAG}" .
docker tag ${DOCKER_REPOSITORY}:${BUILD_TAG} ${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}:${BUILD_TAG}
echo "About to PUSH ${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}:${BUILD_TAG}"
docker push ${DOCKER_REGISTRY}/${DOCKER_REPOSITORY}:${BUILD_TAG}

