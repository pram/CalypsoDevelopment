#!/bin/bash
set -a

SCRIPT=$(readlink -f $0)
SCRIPTPATH=$(dirname $SCRIPT)

CALYPSO_HOME=$(readlink -f $SCRIPTPATH/..)

ME=$CALYPSO_HOME/bin/$(basename "$0")

cd $CALYPSO_HOME

CONFIGFILE=$CALYPSO_HOME/bin/calenv.conf
. $CALYPSO_HOME/bin/setcp.sh
#
#COMMAND OPTIONS
ACTION=$1
ENGINE=$2
MODIFIER=$3
GROUP='-v grep'

#VARIABLES
ENVID=${CALYPSO_HOME##*/}
ENV=$ENVID

################# Get Calypso Variables ##############
. $CALYPSO_HOME/bin/setcp.sh

USER=calypso_user
PASS=`cat $CALYPSO_HOME/../password/$ENV.password`
LOGDIR=$CALYPSO_HOME/../${ENVID}/logs
#RUNAPP="$JAVA_HOME/bin/java -server -d64"
RUNAPP="$JAVA_HOME/bin/java -server -d64 $LAF_BRONZE"

DATE=`date +%Y%m%d%H%M%S`

#OTHER ENGINE OPTIONS
RMI="-Djava.awt.headless=true -Dsun.rmi.transport -Dsun.rmi.transport.tcp.handshakeTimeout=1200000 -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000"
GCOPTIONS="-XX:+TieredCompilation -XX:+UseFastAccessorMethods -Xverify:none -XX:+AlwaysPreTouch -XX:+ScavengeBeforeFullGC -XX:+UseCMSCompactAtFullCollection -XX:+UseBiasedLocking -XX:ParallelGCThreads=4 -Xss2m"
GCLOWPAUSE="-XX:+UseParNewGC -XX:+UseCompressedOops -XX:+UseFastAccessorMethods -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:+CMSIncrementalPacing -XX:CMSIncrementalDutyCycleMin=0 -XX:CMSIncrementalDutyCycle=10 -XX:SurvivorRatio=8 -XX:TargetSurvivorRatio=90 -XX:MaxTenuringThreshold=15"
GCTHROUGHPUT="-XX:+UseCompressedOops -XX:+UseParallelGC -XX:+UseParallelOldGC"
GCLog="-XX:+PrintGCTimeStamps -verbose:gc -XX:-TraceClassUnloading -XX:+PrintGCTimeStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${LOGDIR} -XX:+PrintGCDetails -XX:+PrintTenuringDistribution -XX:+PrintGCApplicationConcurrentTime -XX:+PrintClassHistogram -XX:+PrintGCApplicationStoppedTime -Xloggc:${LOGDIR}/GC.${ENGINE}.${DATE}.log"
JMX="-Dcom.sun.management.jmxremote.port=1300 -Dcom.sun.management.jmxremote.authenticate=false"

#Prepare CLASSPATH
export CLASSPATH=""
export C_CLASSPATH=""

#Set C_CLASSPATH
. ${CALYPSO_HOME}/bin/setcp.sh


enginestart(){

	case "$ENGINE" in

		group)
			GROUP=${MODIFIER}
			awk 'BEGIN{FS="|"} {for(i=1; i<=NF; i++) printf"%s ", $i; print"\n"}' $CONFIGFILE | grep ${GROUP} | while read ENGINENAME GROUP JMXPORT SLEEP XMS XMX PERM MAXNEW NEW CLASS OPTIONS
			do
				$ME start ${ENGINENAME}
			done
			echo "started all Group ${GROUP}"
			exit 0
			;;


		all)
			echo "Error: $ME not possible to $ACTION $ENGINE."
			$ME help
			exit 1
			;;

		"")
			echo "Error: $ME argument missing."
			$ME help
			exit 1
			;;

	esac


	#Start engines
	awk 'BEGIN{FS="|"} {for(i=1; i<=NF; i++) printf"%s ", $i; print"\n"}' $CONFIGFILE | sed -n '/'$ENGINE'/p' | while read ENGINENAME GROUP JMXPORT SLEEP XMS XMX PERM MAXNEW NEW CLASS OPTIONS
	do
		XMS="-Xms${XMS}m"
		XMX="-Xmx${XMX}m"
		PERM="-XX:MaxPermSize=${PERM}m"
		MAXNEW="-XX:MaxNewSize=${MAXNEW}m"
		NEW="-XX:NewSize=${NEW}m"

		if [[ "${MODIFIER}" == "JMX" ]] ; then
	 		JMX="-Dcom.sun.management.jmxremote.port=${JMXPORT} -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
			else JMX=" "
		fi

		echo "	$ENGINENAME"

		case "$ENGINE" in
			EventServer|AuthService|DataServer)
				COMMAND=`echo $RUNAPP $XMS $XMX $PERM $MAXNEW $NEW $JMX $GCLog $GCOPTIONS $GCLOWPAUSE $RMI $JMX $CLASS $OPTIONS -user $USER -password $PASS -env $ENV -nogui -logdir ${LOGDIR}`
				exec $COMMAND &
				sleep $SLEEP
				;;

			"")
				echo "Error: $ME argument missing."
				$ME help
				exit 1
				;;

			*)
				COMMAND=`echo $RUNAPP $XMS $XMX $PERM $MAXNEW $NEW $JMX $GCLog $GCOPTIONS $GCTHROUGHPUT $RMI $JMX $CLASS $OPTIONS -user $USER -password $PASS -env $ENV -nogui -logdir ${LOGDIR}`
				if [[ $GROUP == "BATCH" ]] ; then
					exec $COMMAND &
					cmd_pid=$!
					while ps -p $cmd_pid -o pid= &> /dev/null
					do
						sleep 0
						#wait for process to finish
                                        done
					wait $cmd_pid
					rc=$?
					exit $rc
				fi

				exec $COMMAND &
				sleep $SLEEP
				exit 0
				;;
		esac

	done

}



enginekill(){

	case "$ENGINE" in
		group)
			GROUP=${MODIFIER}
			awk 'BEGIN{FS="|"} {for(i=1; i<=NF; i++) printf"%s ", $i; print"\n"}' $CONFIGFILE | grep ${GROUP} | while read ENGINENAME GROUP JMXPORT SLEEP XMS XMX PERM MAXNEW NEW CLASS OPTIONS
			do
				$ME stop ${ENGINENAME}
			done
			;;

		all)
			PIDS=`$ME PIDs`
			if [ ! -z "$PIDS" ] 
			then
				echo "Killing the following processes:"
				$ME list
				$RUNAPP -Xmx256m -Xms256m -Xss1024k -XX:MaxPermSize=128m  com.calypso.apps.startup.ShutdownAll -user $USER -password $PASS -env $ENV -nogui -logdir ${LOGDIR}
				echo "waiting for process cleanup..."
				sleep 5

			else
				echo "All processes are killed."
				exit 0
			fi

			sleep 2

			PIDS=`$ME PIDs`
			if [ ! -z "$PIDS" ] 
			then
				echo "Force killing processes:"
				$ME list
				kill -9 $PIDS 
			fi
			sleep 2

			PIDS=`$ME PIDs`
			if [ ! -z "$PIDS" ] 
			then
				echo "Could not kill the following processes:"
				$ME list 
				exit 1
			else
				echo "All processes are killed."
			fi
			exit 0
			;;

		"")
			echo "Error: $ME argument missing."
			$ME help
			exit 1
			;;

		*)
			PID=`/bin/ps agwwx | grep -i "$ENGINE " | grep "$ENV " | grep -v grep| grep -v ssh | grep -v calenv | awk '{ print $1 }'`
			
			if [ ! -z "$PID" ] 
			then
				echo -n "Stopping $ENGINE "
				echo ""
				kill $PID
				sleep 5
			else
			echo "$ENGINE not running."
			exit 0
			fi

			PID=`/bin/ps agwwx | grep -i "$ENGINE " | grep "$ENV " | grep -v grep| grep -v ssh | grep -v calenv | awk '{ print $1 }'`
			if [ ! -z "$PID" ] 
			then
				echo -n "Force killing $ENGINE"
				echo ""
				kill -9 $PIDS 

				sleep 5
			fi

			PID=`/bin/ps agwwx | grep -i "$ENGINE " | grep "$ENV " | grep -v grep| grep -v ssh | grep -v calenv | awk '{ print $1 }'`
			if [ ! -z "$PID" ] 
			then
				echo -n "Could not kill $ENGINE"
				echo ""
				$ME list 
				exit 1
				else
					echo "$ENGINE stopped."
			fi
			exit 0
			;;

	esac
}



enginestatus()
{

	case "$ENGINE" in
		all)
			sed -n '/#component/,$p' $CONFIGFILE | awk 'BEGIN{FS="|"} {for(i=1; i<=NF; i++) printf"%s ", $i; print""}' | grep -v "#" | awk '{ print $1 }' | while read ENGINENAME
			do
				$ME list | grep -w $ENGINENAME
				if [ $? -eq 1 ]
				then
					echo "[ -0- ] $ENGINENAME"
				fi
			done

			exit 0
			;;

		"")
			echo "Error: $ME argument missing."
			$ME help
			exit 1
			;;

		*)
			STATUS=`/bin/ps agwwx | grep -i "$ENGINE " | grep "$ENV " | grep -v grep| grep -v ssh | grep -v calenv | awk '{ print $1 }' | head -1` 

			if [ ! -z $STATUS ]
			then
				{
					echo -n "$ENGINE ($ENV) is running."
					echo ""
					for i in $PID; do
						echo PID:$i
					done
					echo ""
				}
			else 
				{
					echo -n "$ENGINE ($ENV) is NOT running."
					echo ""
					exit 1
				}
				fi
			;;

	esac
}


listall()
{
	sed -n '/#component/,$p' $CONFIGFILE | awk 'BEGIN{FS="|"} {for(i=1; i<=NF; i++) printf"%s ", $i; print""}' | grep -v "#" | awk '{ print $1 }' | while read ENGINENAME
	do
		$ME list | grep $ENGINENAME
		if [ $? -eq 1 ]
		then
			echo "[ -0- ] $ENGINENAME"
		fi
	done

}


main()
{
	case "$ACTION" in

		viewall)
			/bin/ps agwwx | grep java | grep $ENV
			exit 0
			;;

		LIST)
			/bin/ps agwwx | grep java | grep "\-env $ENV " | grep -v config | sed 's/[ ]*\([0-9]*\)[ ]*.*calypso[x]*\.apps\.startup\.Start\([a-z|A-Z|0-9]*\)\(.*\)/[\1] \2/g'
			/bin/ps agwwx | grep java | grep "\-env $ENV " | grep config | sed 's/[ ]*\([0-9]*\)[ ]*.*\-config \([a-z|A-Z|0-9]*\)\(.*\)/[\1] \2/g'

			exit 0
			;;

		list)
			$ME LIST | sort -k2 | uniq | tr '\ ' '\t'
			;;

		info)
			echo "LYNX:"
			unzip -q -c ${CALYPSO_HOME}/jars/customjars/LynxCalypsoCustom.jar META-INF/MANIFEST.MF
			echo " "
			echo "IBM:"
			unzip -q -c ${CALYPSO_HOME}/jars/customjars/IBMCalypsoCustom.jar META-INF/MANIFEST.MF
			exit 0
			;;

		PIDs)
			/bin/ps agwwx | grep java | grep "\-env $ENV " | sed 's/[ ]*\([0-9]*\)[ ]*.*calypso[x]*\.apps\.startup\.\([a-z|A-Z|0-9]*\)\(.*\)/ \1 /g' | sort -n -k2 -r
			exit 0
			;;

		start)
			enginestart
			;;

		stop)
			enginekill
			;;

		restart)
			echo "Stopping $ENGINE ..."
			$ME stop $ENGINE
			sleep 2

			echo "Starting $ENGINE ..."
			$ME start $ENGINE
			sleep 2

			$ME status $ENGINE
			;;

		status)
			enginestatus
			;;

		help)
			echo "HELP"
			echo "syntax: $ME start|stop|restart|status|help [group]|all|enginename"
			echo " "
			echo "$ME start enginename		starts enginename"
			echo "$ME start group CORE		starts core engines"
			echo "$ME start group ENGINES		starts all other engines, but not core"
			echo " "
			echo "$ME stop enginename		stops enginename"
			echo "$ME stop group CORE		stops core engines"
			echo "$ME stop group ENGINES
					stops all other engines, but not core"
			echo " "
			echo "$ME restart enginename		stops and then starts enginename"
			echo " "
			echo "$ME status enginename		returns pid of engine, if running"
			echo "$ME status all			returns pids of all running engines"
			;;


		"")
			echo "Error: $ME argument missing."
			$ME help
			exit 1
			;;

		*)
			echo "Error: $ME incorrect argument."
			$ME help
			exit 1
			;;
	esac
}

main
exit $?

