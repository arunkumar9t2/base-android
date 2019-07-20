#!/bin/bash

./gradlew clean

# Publish all modules and their latest versions
./gradlew uploadArchives

# Publish a snapshot of all modules
./gradlew uploadArchives -Psnapshot=true

# Stage all files
git add .
