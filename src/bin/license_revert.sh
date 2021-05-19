#!/bin/sh
LICENSE_HOME=$(dirname $(pwd))
java -Dlog4j.configurationFile=file:${LICENSE_HOME}/conf/log4j2.xml -Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${LICENSE_HOME}/lib/ org.duangframework.license.server.LicenseRevert $1