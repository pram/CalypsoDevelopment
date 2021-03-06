MaCHe - Maven Calypso Helper
============================

A tool to assist with the addition of a Calypso release into a Maven repository. This will be useful if you already have a Maven based Calypso build system and you are looking to quickly get all the release files into your repository quickly.

It will help with the deployment of jars from the main calypso release into your Maven repository by means of creating a script based on the Calypso release jar. It will also create the dependency fragments for the pom file.

Requirements: Groovy 2.0+

    Usage: mache.groovy [settings] <jarfile>
    Options:
     -c,--clean                        Clean working directory  
     -d,--deploy                       Generate Deploy Script otherwise default to Install script  
     -g,--groupName <groupName>        Add to the following group in the repository  
     -h,--help                         Show usage information  
     -l,--label <labelName>            Use the following label in Maven  
     -p,--pom                          Generate dependencies pom fragment to copy into pom.xml  
     -r,--repository <repositoryUrl>   Use the following Maven repository  
     -t,--tempDir <tempDir>            Temp directory to for intermediate files
     -x,--execute                      Execute script to deploy to Maven repository  

