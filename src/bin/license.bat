@echo off
set LICENSE_HOME=%cd%\..
set /p expire=expireTime:
set /p content=content or file:
java -Dlog4j.configurationFile="file:\\%LICENSE_HOME%\conf\log4j2.xml" -Djava.ext.dirs="%LICENSE_HOME%\lib\;%JAVA_HOME%\lib\ext;%JAVA_HOME%\jre\lib\ext" LicenseServer %expire% %content%
pause
exit /b 0