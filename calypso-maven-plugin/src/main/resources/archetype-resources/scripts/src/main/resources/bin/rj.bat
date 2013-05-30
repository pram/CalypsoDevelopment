@echo off
setlocal
set JAVA_ARGS=
if "%CALYPSO_HOME%" == "" set CALYPSO_HOME=%CD%\..
set JAVAEXE=javaw
set P=start

call %CALYPSO_HOME%\bin\setcp.bat

rem Loop Thru the list passed at the command
:start
rem if no parameter exists just continue usually
if [%1] equ [] goto :next else (
rem using /d cmd can be launched using /c option
if /I %1 == /d (
set JAVAEXE=java
set P=start cmd /c

shift
goto :start 
)  
rem using /k cmd can be launced using /K option
if /I %1 == /k (
set P=start cmd /K
shift
goto :start 
) 
rem Run with /debug to catch errors during startup of the application.
if /I %1 == /debug (
set P=
set JAVAEXE=java
shift
goto :start 
) 

rem using -nogui , No GUI will be launched.
if /I %1 == -nogui (
set P=
set JAVAEXE=javaw
set JAVA_ARGS=%JAVA_ARGS% %1
shift
goto :start 
)
rem creation a list excluding above conditions.
set JAVA_ARGS=%JAVA_ARGS% %1


shift
)
goto start
:next

%P% %JAVAEXE% -cp %CLASSPATH% -Xmx128m -XX:MaxPermSize=80m -Dsun.rmi.transport.tcp.handshakeTimeout=1200000 -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000 %LAF% %JAVA_ARGS%
rem echo %P% %JAVAEXE% -Xmx128m -XX:MaxPermSize=128m  -Dsun.rmi.transport.tcp.handshakeTimeout=1200000 -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000 %JAVA_ARGS%

endlocal
