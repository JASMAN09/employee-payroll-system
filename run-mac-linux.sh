#!/usr/bin/env bash
set -e
if ! command -v java >/dev/null 2>&1; then echo "Java JDK 17+ is required. Install a JDK, then retry."; exit 1; fi
if ! command -v mvn >/dev/null 2>&1; then echo "Maven is required for the first build. Open this folder in VS Code and use the Maven for Java extension, or install Apache Maven."; exit 1; fi
mvn clean compile exec:java
