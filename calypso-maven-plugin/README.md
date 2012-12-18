calypso-maven-plugin - Maven and Calypso
==================================================================================

This is a general purpose Maven plugin to help with the development of any Calypso (http://www.calypso.com) related development.

##Installation##

After checking out the project you will need to resolve a couple of dependency issues. `com.sun::tools` and `com.brsanthu::data-exporter` are not registered with Maven Central and must be installed locally (or within your repository) to continue.  

Once this step is complete you can run

    mvn install

to install the plugin into your local .m2 repository. To make the release available to other developers consult the documentation associated with your repository.  

##Archetype Creation##
To create a new Calypso project run the following command

    mvn archetype:generate -DarchetypeGroupId=com.naughtyzombie.calypso.maven -DarchetypeArtifactId=calypso-maven-plugin -DarchetypeVersion=1.0-SNAPSHOT -DgroupId=com.newinstall.calypso -DartifactId=calypsoexample -Dversion=1.0-SNAPSHOT

Replace the values for the parameters `groupID`, `artifactId` and `version` with your desired values. This will create the project with the following structure

    calypsoexample
    |   pom.xml
    |
    +---config
    |   |   pom.xml
    |   |
    |   \---src
    |       \---main
    |           \---config
    |               +---dev
    |               +---prod
    |               \---qa
    +---custom
    |   |   pom.xml
    |   |
    |   \---src
    |       \---main
    |           \---java
    +---hotfixes
    |   |   pom.xml
    |   |
    |   \---src
    |       \---main
    |           +---java
    |           \---resources
    +---releases
    |   |   pom.xml
    |   |
    |   \---src
    |       \---main
    |           \---resources
    \---scripts
        |   pom.xml
        |
        \---src
            \---main
                \---resources

Navigate to the newly created project

    cd calypsoexample

and check that the installation has worked as expected by compiling the project (The project is empty at this stage but should still compile)

    mvn install

You should see something like the snipped below when this process finishes

    [INFO] ------------------------------------------------------------------------
    [INFO] Reactor Summary:
    [INFO]
    [INFO] calypsoexample .................................... SUCCESS [0.000s]
    [INFO] calypsoexample::config ............................ SUCCESS [0.750s]
    [INFO] calypsoexample::custom ............................ SUCCESS [0.047s]
    [INFO] calypsoexample::releases .......................... SUCCESS [0.047s]
    [INFO] calypsoexample::scripts ........................... SUCCESS [0.031s]
    [INFO] calypsoexample::hotfixes .......................... SUCCESS [0.047s]
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 1.078s
    [INFO] Finished at: Tue Dec 18 12:00:11 EET 2012
    [INFO] Final Memory: 5M/15M
    [INFO] ------------------------------------------------------------------------

##Running Calypso Processes##

To see the currently active Calypso processes run

    mvn calypso:status

You should see output of the following form

    +==========+======================================================================+==============================+==============================+
    |   PID    |                              Class Name                              |   ENV (expected[/running])   |            Config            |
    +==========+======================================================================+==============================+==============================+
    | Inactive |              com.calypso.apps.startup.StartEventServer               |              -               |                              |
    | Inactive |              com.calypso.apps.startup.StartAuthService               |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartDataServer               |              -               |                              |
    | Inactive |            com.calypso.apps.startup.StartAccountingEngine            |              -               |                              |
    | Inactive |             com.calypso.apps.startup.StartBalanceEngine              |              -               |                              |
    | Inactive |          com.calypso.apps.startup.StartImportMessageEngine           |              -               |                              |
    | Inactive |         com.calypso.apps.startup.StartIncomingMessageEngine          |              -               |                              |
    | Inactive |            com.calypso.apps.startup.StartInventoryEngine             |              -               |                              |
    | Inactive |              com.calypso.apps.startup.StartLimitEngine               |              -               |                              |
    | Inactive |           com.calypso.apps.startup.StartLiquidationEngine            |              -               |                              |
    | Inactive |         com.calypso.apps.startup.StartMatchableBuilderEngine         |              -               |                              |
    | Inactive |             com.calypso.apps.startup.StartMatchingEngine             |              -               |                              |
    | Inactive |             com.calypso.apps.startup.StartMessageEngine              |              -               |                              |
    | Inactive |              com.calypso.apps.startup.StartSenderEngine              |              -               |                              |
    | Inactive |             com.calypso.apps.startup.StartTransferEngine             |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartTaskEngine               |              -               |                              |
    | Inactive |            com.calypso.apps.startup.StartSchedulingEngine            |              -               |                              |
    | Inactive |             com.calypso.apps.startup.StartMutationEngine             |              -               |                              |
    | Inactive |             com.calypso.apps.startup.StartBillingEngine              |              -               |                              |
    | Inactive |              com.calypso.apps.startup.StartDiaryEngine               |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartMainEntry                |              -               |                              |
    | Inactive |                 com.calypso.apps.startup.StartAdmin                  |              -               |                              |
    | Inactive |                com.calypso.apps.startup.StartUserEnv                 |              -               |                              |
    | Inactive |           com.calypso.apps.startup.StartCalculationServer            |              -               |                              |
    | Inactive |           com.calypso.apps.startup.StartPresentationServer           |              -               |                              |
    | Inactive |             com.calypso.apps.startup.StartMktDataServer              |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartDispatcher               |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartCalculator               |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartExecuteSQL               |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartExecuteSQL               |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartExportData               |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartDBBrowser                |              -               |                              |
    | Inactive |                com.calypso.apps.startup.StartAutoTest                |              -               |                              |
    | Inactive |             com.calypso.apps.startup.StartBenchMarkTools             |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartAccessPerm               |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartSystemEnv                |              -               |                              |
    | Inactive |                com.calypso.web.server.LocalWebServer                 |              -               |                              |
    | Inactive |              com.calypso.apps.startup.StartQuoteServer               |              -               |                              |
    | Inactive |          com.calypso.apps.startup.StartCustomerQuoteServer           |              -               |                              |
    | Inactive |          com.calypso.apps.startup.StartFXManualQuoteServer           |              -               |                              |
    | Inactive |            com.calypso.apps.startup.StartMarginCallEngine            |              -               |                              |
    | Inactive |             com.calypso.apps.startup.StartPositionEngine             |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartCreEngine                |              -               |                              |
    | Inactive |            com.calypso.apps.startup.StartCreSenderEngine             |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartISDAServer               |              -               |                              |
    | Inactive |              com.calypso.apps.datamigration.MigratorGUI              |              -               |                              |
    | Inactive |             com.calypso.apps.datamigration.MigratorCMGUI             |              -               |                              |
    +==========+======================================================================+==============================+==============================+

