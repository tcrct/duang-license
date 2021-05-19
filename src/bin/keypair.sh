#!/bin/sh
LICENSE_HOME=$(dirname $(pwd))
java -Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${LICENSE_HOME}/lib/ org.duangframework.license.server.KeyPairCreator