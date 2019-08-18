#!/bin/bash

# Always publish from master
git checkout master

./gradlew --stop
./gradlew clean

# Publish all modules and their latest versions
./gradlew uploadArchives

# Publish a snapshot of all modules
./gradlew uploadArchives -Psnapshot=true

# Stage all files
git add .

# Commit and publish
git commit -m "[RELEASE] Publish artifacts" && git push