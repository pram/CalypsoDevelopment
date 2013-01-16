#!/bin/sh

# WARNING
# When modifying classpaths, be sure the corresponding windows paths 
# in calypso.bat are also updated
####################################################################

if [ "x$CALYPSO_HOME" == "x" ]; then
	CALYPSO_HOME=`pwd`/..
fi

OLDCLASSPATH=$CLASSPATH

CALYPSO_LIB=$CALYPSO_HOME/jars

CLASSPATH=`cat classpath.unix`

export CLASSPATH
