#!/bin/sh
LICENSE_HOME=$(dirname $(pwd))
java -Dlog4j.configurationFile=file:${LICENSE_HOME}/conf/log4j2.xml -Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${LICENSE_HOME}/lib/ org.duangframework.license.server.LicenseServer 1b88ab6d 1Y MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKJgdtSCbftLiqgxW2LsZPcru4YVWnh1YULMjItH5M0rEBA4fyRmYVBWWpG5tLRmGDuG4OcdS1KrRB9R8akeNQuWpkCHcE49PTYrPlZIo