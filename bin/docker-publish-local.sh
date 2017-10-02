#!/usr/bin/env bash
#Script is for building a docker image to run in a local docker environment.
#Local docker environment is set to the test build profile.

export BUILD_PROFILE=local

bash ./bin/docker-publish-cloud.sh ${1}
