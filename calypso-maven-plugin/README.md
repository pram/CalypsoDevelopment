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



