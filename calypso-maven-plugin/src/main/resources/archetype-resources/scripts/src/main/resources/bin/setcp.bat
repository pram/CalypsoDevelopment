if "%CALYPSO_HOME%" == "" set CALYPSO_HOME=%CD%\..

set OLDCLASSPATH=%CLASSPATH%

REM FOR /F "usebackq" %%i IN (`more classpath.windows`) DO @set CLASSPATH=%%i

set CLASSPATH="..\jars\*";%CALYPSO_HOME%\resources;%CALYPSO_HOME%\resources\appconfig;%CALYPSO_HOME%\resources\log4jconfig

if NOT "%OLDCLASSPATH%a"=="a" set CLASSPATH=%CLASSPATH%;%OLDCLASSPATH%

