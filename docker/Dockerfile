FROM ubuntu:24.04

LABEL org.opencontainers.image.authors="hi@arunkumar.dev"
LABEL org.opencontainers.image.source="https://github.com/arunkumar9t2/base-android"

# Install common software
RUN apt-get update && apt-get install -y --no-install-recommends \
    apt-transport-https \
    ca-certificates \
    curl \
    g++ \
    gcc \
    git \
    git-extras \
    gnupg \
    gzip \
    locales \
    openjdk-17-jdk \
    software-properties-common \
    sudo \
    tar \
    unzip \
    wget \
    zip \
    && rm -rf /var/lib/apt/lists/*

# Set en locale
RUN locale-gen en_US.UTF-8
ENV LANG=C.UTF-8

# Setup Android SDK
ENV ANDROID_CMDLINE_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-13114758_latest.zip"
ENV ANDROID_HOME="/opt/android-sdk"
ENV ANDROID_SDK_ROOT=$ANDROID_HOME \
    PATH=${PATH}:${ANDROID_HOME}/cmdline-tools/latest:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools

RUN mkdir "$ANDROID_SDK_ROOT" && \
    mkdir -p "$ANDROID_SDK_ROOT/.android" && \
    mkdir -p "$ANDROID_SDK_ROOT/cmdline-tools" && \
    curl -o commandlinetools.zip $ANDROID_CMDLINE_TOOLS_URL && \
    unzip commandlinetools.zip -d "$ANDROID_SDK_ROOT/cmdline-tools" && \
    mv "$ANDROID_SDK_ROOT/cmdline-tools/cmdline-tools" "$ANDROID_SDK_ROOT/cmdline-tools/latest" && \
    rm commandlinetools.zip && \
    # Setup sdkmanager
    yes | sdkmanager --licenses && \
    sdkmanager tools platform-tools "platforms;android-30" "platforms;android-35" "build-tools;30.0.3" "build-tools;35.0.0" --verbose

ENV GRADLE_USER_HOME=/github/home/.gradle
