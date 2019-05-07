#!/bin/bash

# Publish all modules and their latest versions
./gradlew uploadArchives

# Purge all active snapshots
echo "Cleaning existing snapshots"
rm -rf maven-repo\snapshots\

# Publish a snapshot of all modules
./gradlew uploadArchives -PisSnapshot=true
