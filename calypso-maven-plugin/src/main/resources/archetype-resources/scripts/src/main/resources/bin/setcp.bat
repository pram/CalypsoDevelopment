if "%CALYPSO_HOME%" == "" set CALYPSO_HOME=%CD%\..
if "%CALYPSO_ENV_NAME%" == "" for /f "delims=" %%A in ('getenv') do SET CALYPSO_ENV_NAME=%%A

echo CALYPSO_ENV_NAME = %CALYPSO_ENV_NAME%

set OLDCLASSPATH=%CLASSPATH%

set CLASSPATH="..\jars\*";%CALYPSO_HOME%\resources;%CALYPSO_HOME%\resources\appconfig;%CALYPSO_HOME%\resources\log4jconfig

if exist laf.%CALYPSO_ENV_NAME% set /p LAF=< laf.%CALYPSO_ENV_NAME%

echo %LAF%

if NOT "%OLDCLASSPATH%a"=="a" set CLASSPATH=%CLASSPATH%;%OLDCLASSPATH%
