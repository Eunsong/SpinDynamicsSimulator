#! /usr/bin/bash

javac -cp ../:/usr/share/java/junit4-4.11.jar BuilderTest.java
java -cp .:../:/usr/share/java/junit4-4.11.jar org.junit.runner.JUnitCore BuilderTest
