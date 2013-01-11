calypso-maven-plugin - Calypso development with Maven
==================================================================================

This is a general purpose Maven plugin to help with the development of any Calypso (http://www.calypso.com) related project.

##Installation##

After checking out the project you will need to resolve a couple of dependency issues. `com.sun::tools` and `com.brsanthu::data-exporter` are not registered with Maven Central and must be installed locally (or within your repository) to continue.  You must also install MaCHe (https://github.com/pram/CalypsoDevelopment/tree/master/MaCHe) into your local/remote repository in order to use the release module.

Once this step is complete you can run

    mvn install

to install the plugin into your local .m2 repository. To make the release available to other developers consult the remote deployment documentation associated with your repository.

##Archetype Creation##
To create a new Calypso project run the following command

    mvn archetype:generate -DarchetypeGroupId=com.naughtyzombie.calypso.maven -DarchetypeArtifactId=calypso-maven-plugin -DarchetypeVersion=1.0-SNAPSHOT -DgroupId=com.newinstall.calypso -DartifactId=calypsoexample -Dversion=1.0-SNAPSHOT

Replace the values for the parameters `groupID`, `artifactId` and `version` with your desired values. You will also be asked to enter the Calypso version that you will be working with. You can either add the following

    -DcalypsoVerision=XXxxxx[SPy]

to the archetype generation command above, or you will be asked to enter the version number interactively eg

    Define value for property 'calypsoVersion': : 120000SP6

This will create the project with the following structure.


    calypsoexample
    |   pom.xml
    |
    +---config
    |   |   pom.xml
    |   |
    |   \---src
    |       \---main
    |           \---config
    |           \---resources
    |
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

Copy the Calypso release jar that corresponds to the version number entered above into the `releases/src/main/resources` directory.  

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

To see the processes in the other process catalog included in the release either modify the `processCatalog` in the parent pom (currently set to `<processCatalog>FULL</processCatalog>`) or run `mvn calypso:status` setting the `processCatalog` variable to the name of the alternative catalog. For example to see the catalog of the other bundled catalog (named ALL) you would run the following command

    mvn calypso:status -DprocessCatalog=ALL

The output of which looks like

    +==========+======================================================================+==============================+==============================+
    |   PID    |                              Class Name                              |   ENV (expected[/running])   |            Config            |
    +==========+======================================================================+==============================+==============================+
    | Inactive |              com.calypso.apps.startup.StartAuthService               |              -               |                              |
    | Inactive |               com.calypso.apps.startup.StartDataServer               |              -               |                              |
    | Inactive |              com.calypso.apps.startup.StartEventServer               |              -               |                              |
    | Inactive |           com.calypso.apps.startup.StartCalculationServer            |              -               |           CS_AdHoc           |
    | Inactive |           com.calypso.apps.startup.StartCalculationServer            |              -               |         CS_Official          |
    | Inactive |           com.calypso.apps.startup.StartPresentationServer           |              -               |           PS_AdHoc           |
    | Inactive |           com.calypso.apps.startup.StartPresentationServer           |              -               |         PS_Official          |
    +==========+======================================================================+==============================+==============================+

You can define your own catalogs in the `config` module. They must be named `calypso-catalog-env.xml` or `calypso-catalog-xxx.xml` where xxx corresponds to the calypso.env.name property ie

    mvn calypso:status -DprocessCatalog=env -Dcalypso.env.name=DEV1

##Displaying the Calypso Dependency Tree##

In order to see the dependency graph defined in the process catalog you can run the `tree` goal. So running

    mvn calypso:tree

Will show

    -- EventServer'{ className='com.calypso.apps.startup.StartEventServer'}
       `-- AuthService'{ className='com.calypso.apps.startup.StartAuthService'}
          `-- DataServer'{ className='com.calypso.apps.startup.StartDataServer'}
             |-- AccountingEngine'{ className='com.calypso.apps.startup.StartAccountingEngine'}
             |-- BalanceEngine'{ className='com.calypso.apps.startup.StartBalanceEngine'}
             |-- ImportMessageEngine'{ className='com.calypso.apps.startup.StartImportMessageEngine'}
             |-- IncomingMessageEngine'{ className='com.calypso.apps.startup.StartIncomingMessageEngine'}
             |-- InventoryEngine'{ className='com.calypso.apps.startup.StartInventoryEngine'}
             |-- LimitEngine'{ className='com.calypso.apps.startup.StartLimitEngine'}
             |-- LiquidationEngine'{ className='com.calypso.apps.startup.StartLiquidationEngine'}
             |-- MatchableBuilderEngine'{ className='com.calypso.apps.startup.StartMatchableBuilderEngine'}
             |-- MatchingEngine'{ className='com.calypso.apps.startup.StartMatchingEngine'}
             |-- MessageEngine'{ className='com.calypso.apps.startup.StartMessageEngine'}
             |-- SenderEngine'{ className='com.calypso.apps.startup.StartSenderEngine'}
             |-- TransferEngine'{ className='com.calypso.apps.startup.StartTransferEngine'}
             |-- TaskEngine'{ className='com.calypso.apps.startup.StartTaskEngine'}
             |-- SchedulingEngine'{ className='com.calypso.apps.startup.StartSchedulingEngine'}
             |-- MutationEngine'{ className='com.calypso.apps.startup.StartMutationEngine'}
             |-- BillingEngine'{ className='com.calypso.apps.startup.StartBillingEngine'}
             |-- DiaryEngine'{ className='com.calypso.apps.startup.StartDiaryEngine'}
             |-- MainEntry'{ className='com.calypso.apps.startup.StartMainEntry'}
             |-- Admin'{ className='com.calypso.apps.startup.StartAdmin'}
             |-- UserEnv'{ className='com.calypso.apps.startup.StartUserEnv'}
             |-- CalculationServer'{ className='com.calypso.apps.startup.StartCalculationServer'}
             |-- PresentationServer'{ className='com.calypso.apps.startup.StartPresentationServer'}
             |-- MktDataServer'{ className='com.calypso.apps.startup.StartMktDataServer'}
             |-- Dispatcher'{ className='com.calypso.apps.startup.StartDispatcher'}
             |-- Calculator'{ className='com.calypso.apps.startup.StartCalculator'}
             |-- ExecuteSQL'{ className='com.calypso.apps.startup.StartExecuteSQL'}
             |-- MiddleTierExecuteSQL'{ className='com.calypso.apps.startup.StartExecuteSQL'}
             |-- ExportData'{ className='com.calypso.apps.startup.StartExportData'}
             |-- DBBrowser'{ className='com.calypso.apps.startup.StartDBBrowser'}
             |-- AutoTest'{ className='com.calypso.apps.startup.StartAutoTest'}
             |-- BenchMarkTools'{ className='com.calypso.apps.startup.StartBenchMarkTools'}
             |-- AccessPerm'{ className='com.calypso.apps.startup.StartAccessPerm'}
             |-- SystemEnv'{ className='com.calypso.apps.startup.StartSystemEnv'}
             |-- LocalWebServer'{ className='com.calypso.web.server.LocalWebServer'}
             |-- QuoteServer'{ className='com.calypso.apps.startup.StartQuoteServer'}
             |-- CustomerQuoteServer'{ className='com.calypso.apps.startup.StartCustomerQuoteServer'}
             |-- FXManualQuoteServer'{ className='com.calypso.apps.startup.StartFXManualQuoteServer'}
             |-- MarginCallEngine'{ className='com.calypso.apps.startup.StartMarginCallEngine'}
             |-- PositionEngine'{ className='com.calypso.apps.startup.StartPositionEngine'}
             |-- CreEngine'{ className='com.calypso.apps.startup.StartCreEngine'}
             |-- CreSenderEngine'{ className='com.calypso.apps.startup.StartCreSenderEngine'}
             |-- ISDAServer'{ className='com.calypso.apps.startup.StartISDAServer'}
             |-- CAM'{ className='com.calypso.apps.datamigration.MigratorGUI'}
             `-- ConfigurationManagement'{ className='com.calypso.apps.datamigration.MigratorCMGUI'}

Alternatively running for the other bundled catalog

    mvn calypso:tree -DprocessCatalog=ALL

Will show

    -- EventServer'{ className='com.calypso.apps.startup.StartEventServer'}
       `-- AuthService'{ className='com.calypso.apps.startup.StartAuthService'}
          `-- DataServer'{ className='com.calypso.apps.startup.StartDataServer'}
             |-- CalculationServer'{ className='com.calypso.apps.startup.StartCalculationServer', id='CS_AdHoc'}
             |   `-- PresentationServer'{ className='com.calypso.apps.startup.StartPresentationServer', id='PS_AdHoc'}
             `-- CalculationServer'{ className='com.calypso.apps.startup.StartCalculationServer', id='CS_Official'}
                `-- PresentationServer'{ className='com.calypso.apps.startup.StartPresentationServer', id='PS_Official'}

If you want to see the process catalog for the sample catalog included in the config module then type the following

    mvn calypso:tree -DprocessCatalog=env -Dcalypso.env.name=DEV1

This will display

    -- EventServer'{ className='com.calypso.apps.startup.StartEventServer'}
   `-- AuthService'{ className='com.calypso.apps.startup.StartAuthService'}
      `-- DataServer'{ className='com.calypso.apps.startup.StartDataServer'}
         |-- CalculationServer'{ className='com.calypso.apps.startup.StartCalculationServer', id='CS_AdHoc'}
         |   `-- PresentationServer'{ className='com.calypso.apps.startup.StartPresentationServer', id='PS_AdHoc'}
         |-- CalculationServer'{ className='com.calypso.apps.startup.StartCalculationServer', id='CS_Official'}
         |   `-- PresentationServer'{ className='com.calypso.apps.startup.StartPresentationServer', id='PS_Official'}
         |-- TransferEngine'{ className='com.calypso.apps.startup.StartTransferEngine'}
         `-- TaskEngine'{ className='com.calypso.apps.startup.StartTaskEngine'}

To see output for a custom process catalog, then replace `DEV1` above with the desired catalog name. Ensure that the file calypso-catalog-<env>.xml exists on the classpath.
