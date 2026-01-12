#!/bin/bash/

rm -rf bin/*

javac -d bin $(find src -name "*.java")

jar xf Remnants.jar

jar uf Remnants.jar -C bin .

java -jar Remnants.jar