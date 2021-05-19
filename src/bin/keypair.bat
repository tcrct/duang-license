@echo off
set LICENSE_HOME=%cd%\..
java -Dlog4j.configurationFile="file:\\%LICENSE_HOME%\conf\log4j2.xml" -Djava.ext.dirs="%LICENSE_HOME%\lib\;%JAVA_HOME%\lib\ext;%JAVA_HOME%\jre\lib\ext" KeyPairCreator
pause
exit /b 0