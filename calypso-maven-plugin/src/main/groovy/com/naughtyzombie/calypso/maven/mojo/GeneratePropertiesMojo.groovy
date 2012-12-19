package com.naughtyzombie.calypso.maven.mojo

import org.codehaus.gmaven.mojo.GroovyMojo
import org.apache.tools.ant.types.selectors.TypeSelector

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 18/12/12
 * Time: 16:44
 */

/**
 * Generate the calypsosystem.properties files
 *
 * @goal generate-properties
 */
class GeneratePropertiesMojo extends GroovyMojo {

    /**
     * The directory where source properties are located
     *
     * To override use -Dsrc="blah"
     *
     * @parameter property="src" default-value="${project.basedir}/src/main/config"
     */
    private String src;

    /**
     * The directory where source properties are located
     *
     * To override use -Dout="blah"
     *
     * @parameter property="out" default-value="${project.build.directory}/generated-sources"
     */
    private String out;

    /**
     * The directory where template files are stored
     *
     * To override use -DtemplateFolder="blah"
     *
     * @parameter property="templateFolder" default-value="templates"
     */
    private String templateFolder;

    /**
     * The calypso release version for the templates
     *
     * To override use -Dversion="blah"
     *
     * @parameter property="version" default-value="130003SP1"
     */
    private String version;

    @Override
    void execute() {

        def templateDir = new File(src + "/" + templateFolder)
        def versionPattern = ~/.*\.$version/

        templateDir.eachFile {templateFile ->
            if (templateFile ==~ versionPattern) {

                def replaceSet = [] as Set

                templateFile.eachLine {line ->
                    def pattern = /(@)([a-zA-Z].+)@/
                    if (line =~ pattern) {
                        def matcher = line =~ pattern
                        replaceSet.add(matcher[0][2])
                    }
                }

                replaceSet = new TreeSet(replaceSet)

                log.info("Expected Tokens")
                replaceSet.each {replacementToken -> log.info(replacementToken)}

                log.info("Looking for source properties files")
                new File(src).eachFile {propertiesFile ->
                    if (propertiesFile.isFile()) {
                        log.info("Found " + propertiesFile)
                        def env = propertiesFile.name.lastIndexOf('.').with {it != -1 ? propertiesFile.name[0..<it] : propertiesFile.name}
                        def filePrefix = templateFile.name.lastIndexOf('.').with {it != -1 ? templateFile.name[0..<it] : templateFile.name}
                        def envFileName = filePrefix + "." + env
                        new AntBuilder().delete(file: out + "/" + envFileName)
                        new AntBuilder().copy(file: templateFile,
                                tofile: out + "/" + envFileName)

                        def properties = new Properties()
                        propertiesFile.withInputStream {stream -> properties.load(stream)}

                        def replacementFile = new File(out, envFileName)
                        String contents = replacementFile.getText()

                        properties.each {prop ->
                            log.info(prop.key + " = " + prop.value)
                            contents = contents.replaceAll("@" + prop.key + "@", prop.value.toString())
                        }

                        replacementFile.write(contents)

                        def common = replaceSet.intersect(properties.keySet())
                        def diff = replaceSet.plus(properties.keySet())
                        diff.removeAll(common)

                        diff.each {missing -> log.warn("Missing from properties file " + missing)}
                    }
                }

            }
        }
    }
}
