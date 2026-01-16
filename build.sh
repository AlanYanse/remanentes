#!/bin/bash/

rm -rf bin/*

javac -d bin $(find src -name "*.java")

jar xf Remnants.jar

jar uf Remnants.jar -C bin .

jar uf Remnants.jar -C src .

java -jar Remnants.jar