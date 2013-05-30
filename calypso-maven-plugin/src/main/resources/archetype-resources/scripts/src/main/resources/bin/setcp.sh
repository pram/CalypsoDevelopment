#!/bin/bash

# WARNING
# When modifying classpaths, be sure the corresponding windows paths 
# in calypso.bat are also updated

if [ "x$CALYPSO_HOME" == "x" ]; then

	SCRIPT=$(readlink -f $0)
	SCRIPTPATH=$(dirname $SCRIPT)

	CALYPSO_HOME=$SCRIPTPATH/..
fi

#CLASSPATH=`cat $CALYPSO_HOME/bin/classpath.unix`
CLASSPATH=$(eval echo -e `< $CALYPSO_HOME/bin/classpath.unix`)

CLASSPATH="\
$CLASSPATH\
:$CALYPSO_HOME/resources\
:$CALYPSO_HOME/resources/appconfig\
:$CALYPSO_HOME/resources/log4jconfig\
"

export CLASSPATH
