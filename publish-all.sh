#!/bin/bash

# Publish all modules and their latest versions
./gradlew uploadArchives

# Publish a snapshot of all modules
./gradlew uploadArchives -PisSnapshot=true
