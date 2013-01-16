if "%CALYPSO_HOME%" == "" set CALYPSO_HOME=%CD%\..

set OLDCLASSPATH=%CLASSPATH%

REM FOR /F "usebackq" %%i IN (`more classpath.windows`) DO @set CLASSPATH=%%i

set CLASSPATH=..\jars\*

if NOT "%OLDCLASSPATH%a"=="a" set CLASSPATH=%CLASSPATH%;%OLDCLASSPATH%

