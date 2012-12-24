package com.naughtyzombie.calypso.maven.mojo

import org.codehaus.gmaven.mojo.GroovyMojo
import com.naughtyzombie.mache.Worker
import com.naughtyzombie.mache.Settings
import org.apache.maven.project.MavenProject

/**
 * Created with IntelliJ IDEA.
 * User: Pram Attale
 * Date: 19/12/12
 * Time: 17:25
 */

/**
 * Install the Calypso jars into the local maven repository
 *
 * @goal install-calypso-jars
 */
class InstallCalypsoJarsMojo extends GroovyMojo {

    /**
     * This should only be run against modules that are for management purposes
     *
     * @parameter property="managementModule" default-value="false"
     */
    private boolean managementModule;

    /**
     * The maven project.
     *
     * @parameter property="project"
     * @readonly
     */
    private MavenProject project;

    /**
     * Calypso version number
     *
     * @parameter property="version" default-value="${calypso.version}"
     */
    private String version;

    /**
     * Calypso version number
     *
     * @parameter property="group" default-value="${calypso.group}"
     */
    private String group;

    /**
     * Maven installation directory
     *
     * @parameter property="mavenHome" default-value="${env.M2_HOME}"
     */
    private String mavenHome

    /**
     * Unpacked release source directory
     *
     * @parameter property="unpackedLocation" default-value="${calypso.unpacked.release}"
     */
    private String unpackedLocation

    /**
     * Temp dir
     *
     * @parameter property="buildDir" default-value="${project.build.directory}/mache"
     */
    private String buildDir

    @Override
    void execute() {
        if (managementModule) {
            def settings = new Settings()
            settings.setClean(true)
            settings.setGeneratePom(true)
            settings.setDeploy(false)
            settings.setTmpDir(buildDir)
            settings.setLabel(version)
            settings.setGroup(group)
            //TODO derive the value for the repository from that set in the parent pom
            settings.setRepository("dummy")
            if (mavenHome==null) {
                getLog().error("M2_HOME not set.")
                fail("Set M2_HOME Environment Variable")
            }
            settings.setMavenLocation(mavenHome)

            Worker worker = new Worker(settings, new File(buildDir))
            worker.cleanUp()
            worker.copyJars(unpackedLocation)
            worker.generateInstallScript()
            worker.generateDeployScript()
            worker.generatePomFragment()
        }
    }
}
