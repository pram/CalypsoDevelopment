#!/bin/sh

# determine where runjava.sh is running
# from, this will be the dir
########################################
CWD=`dirname $0`
cd $CWD/..

CALYPSO_HOME=`pwd`

if [ ! -d "$CALYPSO_HOME/jars" ] ; then
	echo "Unable to determine CALYPSO_HOME"
	exit
fi

. $CALYPSO_HOME/bin/setcp.sh

echo 'CALYPSO_HOME=' $CALYPSO_HOME
echo 'CLASSPATH= ' $CLASSPATH
java -server -XX:MaxPermSize=80m -Xmx256m -Dsun.rmi.transport.tcp.handshakeTimeout=1200000 -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000 $* -log &
