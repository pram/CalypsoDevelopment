#!/bin/sh

# WARNING
# When modifying classpaths, be sure the corresponding windows paths 
# in calypso.bat are also updated
####################################################################

if [ "x$CALYPSO_HOME" == "x" ]; then
	CALYPSO_HOME=/usr/local/software/calypso
fi

OLDCLASSPATH=$CLASSPATH

CALYPSO_LIB=$CALYPSO_HOME/jars
THIRDPARTHY_LIB=$CALYPSO_LIB/thirdparty
JAXB_LIB=$CALYPSO_LIB/jaxb2/lib

JAXB_JARS="\
:$CALYPSO_LIB/jaxb-impl-runtime.jar\
:$CALYPSO_LIB/swift-xml.jar\
:$CALYPSO_LIB/swift-xml_jaxb.jar\
"

THIRDPARTY_JARS="\
:$THIRDPARTHY_LIB/commons/*\
:$THIRDPARTHY_LIB/eai/*\
:$THIRDPARTHY_LIB/j2ee/*\
:$THIRDPARTHY_LIB/jaxb2/lib/*\
:$THIRDPARTHY_LIB/jdbcdrivers/*\
:$THIRDPARTHY_LIB/jsonparsers/*\
:$THIRDPARTHY_LIB/langparsers/*\
:$THIRDPARTHY_LIB/logging/*\
:$THIRDPARTHY_LIB/mathtools/*\
:$THIRDPARTHY_LIB/messaging/*\
:$THIRDPARTHY_LIB/orm/*\
:$THIRDPARTHY_LIB/quartz/*\
:$THIRDPARTHY_LIB/others/*\
:$THIRDPARTHY_LIB/reportingtools/*\
:$THIRDPARTHY_LIB/spring/*\
:$THIRDPARTHY_LIB/testtools/*\
:$THIRDPARTHY_LIB/web/*\
:$THIRDPARTHY_LIB/xmltools/*\
"

# The following list of jars are not 
# distributed by Calypso and are added
# by clients on-site.  For development they exist
# in the tree in the above directories
THIRDPARTY_NOTSHIPPED="\
:$CALYPSO_LIB/ojdbc6.jar\
:$CALYPSO_LIB/jconn4.jar\
:$CALYPSO_LIB/j3dcore.jar\
:$CALYPSO_LIB/j3dutils.jar\
:$CALYPSO_LIB/jazz.jar\
:$CALYPSO_LIB/jazz.zip\
:$CALYPSO_LIB/JFlex.jar\
:$CALYPSO_LIB/jzlib.jar\
:$CALYPSO_LIB/objenesis.jar\
:$CALYPSO_LIB/rfa.jar\
:$CALYPSO_LIB/parallelcolt.jar\
"

CALYPSOML="\
:$CALYPSO_LIB/calypsoml-impl.jar\
:$CALYPSO_LIB/calypsoml-core.jar\
:$CALYPSO_LIB/calypsoml-bloomberg-impl.jar\
:$CALYPSO_LIB/calypsoml-reporting-impl.jar\
:$CALYPSO_LIB/calypsoml-calculationserver-impl.jar\
:$CALYPSO_LIB/calypsoml-erslimits-impl.jar\
:$CALYPSO_LIB/calypsoml-pricingsheet-impl.jar\
"

GALAPAGOS="\
:$CALYPSO_LIB/galapagos.jar\
:$CALYPSO_LIB/galapagos_jaxb.jar\
"

VISOKIO="\
:$CALYPSO_LIB/visokio.jar\
:$CALYPSO_LIB/generator-api-genapi-v1.0-b6.jar\
"

SWIFTSIMULATOR="\
:$CALYPSO_LIB/swiftsimulator.jar\
:$CALYPSO_LIB/swiftsimulator_jaxb.jar\
"

JIDE_LIBS=$CALYPSO_LIB/jide/lib
JIDE_JARS="\
:$JIDE_LIBS/*\
"

TRAX="\
:$CALYPSO_LIB/trax.jar\
:$CALYPSO_LIB/trax_jaxb.jar\
"

XPROD="\
:$CALYPSO_LIB/xprod.jar\
:$CALYPSO_LIB/xprod_jaxb.jar\
"

STRUCTURINGTOOL="\
:$CALYPSO_LIB/structuringtool.jar\
"
CALIBRATION="\
:$CALYPSO_LIB/calibration.jar\
"

SYMPHONY="\
:$CALYPSO_LIB/calypso-symphony.jar\
:$CALYPSO_LIB/JavaSoamApi.jar\
"

OMS="\
:$CALYPSO_LIB/oms.jar\
:$CALYPSO_LIB/fix.jar\
:$CALYPSO_LIB/command.jar\
:$CALYPSO_LIB/quickfixj-all-1.4.0.jar\
:$CALYPSO_LIB/mina-core-1.1.7.jar\
"

LIQUIDITY="\
:$CALYPSO_LIB/liquidity-reporting.jar\
:$CALYPSO_LIB/liquidity-core.jar\
"

CLASSPATH="\
:$CALYPSO_LIB/calypsoCustom.jar\
:$CALYPSOML\
:$CALYPSO_LIB/markit.jar\
:$CALYPSO_LIB/markit_jaxb.jar\
:$CALYPSO_LIB/cal-upload.jar\
:$CALYPSO_LIB/datauploader.jar\
:$CALYPSO_LIB/markitwire.jar\
:$CALYPSO_LIB/HTTPClient.jar\
:$CALYPSO_LIB/swapswire.jar\
:$CALYPSO_LIB/swml_jaxb.jar\
:$CALYPSO_LIB/sw_dealsink.jar\
:$CALYPSO_LIB/bloomberg.jar\
:$CALYPSO_LIB/marketconformity.jar\
:$CALYPSO_LIB/mktdata-viewer.jar\
:$CALYPSO_LIB/reuters-rfa.jar\
:$CALYPSO_LIB/tof.jar\
:$CALYPSO_LIB/mod_ers.jar\
:$CALYPSO_LIB/schemer.jar\
:$CALYPSO_LIB/analysis-metadata.jar\
:$CALYPSO_LIB/calculation-server.jar\
:$CALYPSO_LIB/presentation-server.jar\
:$CALYPSO_LIB/trade-blotter.jar\
:$CALYPSO_LIB/executesql.jar\
:$CALYPSO_LIB/schemer_jaxb.jar\
:$CALYPSO_LIB/dbbrowser.jar\
:$CALYPSO_LIB/dbimportexport.jar\
:$CALYPSO_LIB/mktdataserver.jar\
:$CALYPSO_LIB/persistence-service.jar\
:$CALYPSO_LIB/hibernate-impl.jar\
:$CALYPSO_LIB/hibernate-core.jar\
:$CALYPSO_LIB/hedge-accounting.jar\
:$CALYPSO_LIB/taskenrichment.jar\
:$CALYPSO_LIB/taskstation.jar\
:$CALYPSO_LIB/irdpricing.jar\
:$CALYPSO_LIB/calypso-logging.jar\
:$CALYPSO_LIB/calibintegration.jar\
:$CALYPSO_LIB/calypso-pricingscript-example.jar\
:$CALYPSO_LIB/calypso-pricingscript.jar\
:$CALYPSO_LIB/calib.jar\
:$CALYPSO_LIB/calypso-random.jar\
:$CALYPSO_LIB/calypso-graphics.jar\
:$CALYPSO_LIB/scriptableOTC.jar\
:$CALYPSO_LIB/pricingsheet.jar\
:$CALYPSO_LIB/pricinggrid.jar\
:$CALYPSO_LIB/timehorizonsimulation.jar\
:$CALYPSO_LIB/deal-station.jar\
:$CALYPSO_LIB/bondexoticnote.jar\
:$CALYPSO_LIB/crdpricing.jar\
:$CALYPSO_LIB/cws.jar\
:$CALYPSO_LIB/mbs.jar\
:$CALYPSO_LIB/calypso-scheduling-engine.jar\
:$GALAPAGOS\
:$VISOKIO\
:$SWIFTSIMULATOR\
:$TRAX\
:$OMS\
:$CALIBRATION\
:$STRUCTURINGTOOL\
:$XPROD\
:$LIQUIDITY\
:$CALYPSO_HOME/resources\
:$CALYPSO_HOME/resources/appconfig\
:$CALYPSO_HOME/resources/log4jconfig\
:$CALYPSO_HOME/build\
:$CALYPSO_LIB/calypso-core.jar\
:$CALYPSO_LIB/calypso-appkit.jar\
:$CALYPSO_LIB/calypso-swingx.jar\
:$CALYPSO_LIB/calypso.jar\
:$CALYPSO_HOME\
:$JAXB_JARS\
:$CALYPSO_LIB/twsutil.jar\
:$CALYPSO_LIB/intex.jar\
:$CALYPSO_LIB/sqlinputvalidator.jar\
:$JIDE_JARS\
:$THIRDPARTY_JARS\
:$THIRDPARTY_NOTSHIPPED\
:$OLDCLASSPATH\
"

export CLASSPATH
