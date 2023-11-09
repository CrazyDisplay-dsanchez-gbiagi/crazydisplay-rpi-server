#!/bin/bash

# run.sh

# Change the working directory to where the script is located
cd "$(dirname "$0")"

# Set MAVEN_OPTS environment variable
export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED"

# Check for the first argument and set it as the main class
mainClass="$1"

echo "Setting MAVEN_OPTS to: $MAVEN_OPTS"
echo "Main Class: $mainClass"

# Remove the first argument (mainClass) so the rest can be passed to Maven
shift

# Construct Maven argument for the main class
mavenMainClassArg="-Dexec.mainClass=$mainClass"

# Gather all remaining arguments into a string
javaArgs="$*"

# Construct argument for passing additional args to Java program
execArgsForJava="-Dexec.args='$javaArgs'"

echo "Maven Main Class Argument: $mavenMainClassArg"
echo "Java Program Arguments: $javaArgs"

# Execute mvn command
mvn clean test-compile
mvn exec:java $mavenMainClassArg $execArgsForJava
