@echo off
mvn compile && mvn process-classes && mvn package && mvn install
