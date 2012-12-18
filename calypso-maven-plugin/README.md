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

You should see something like the snippet below when this process finishes

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



