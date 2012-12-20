package com.naughtyzombie.mache

/**
 * Created with IntelliJ IDEA.
 * User: Pram Attale
 * Date: 2012/07/24
 * Time: 10:24 AM
 */
public class Settings {
    private String label
    private String repository
    private String group
    private boolean execute
    private boolean clean
    private boolean generatePom
    private boolean deploy
    private String jarFile
    private String mvnLocation
    private String tmpDir

    Settings() {
    }

    def setLabel(String label) {
        this.label = label
    }

    String getLabel() {
        return this.label
    }

    def setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir
    }

    String getTmpDir() {
        return this.tmpDir
    }

    def setRepository(String repository) {
        this.repository = repository
    }

    String getRepository() {
        return this.repository
    }

    def setGroup(String group) {
        this.group = group
    }

    String getGroup() {
        return this.group
    }

    def setJarFile(String jarFile) {
        this.jarFile = jarFile
    }

    String getJarFile() {
        return this.jarFile
    }

    def setExecute(boolean execute) {
        this.execute = execute
    }

    boolean isExecute() {
        return this.execute
    }

    boolean isDeploy() {
        return this.deploy
    }

    def setDeploy(boolean deploy) {
        this.deploy = deploy
    }

    def setClean(boolean clean) {
        this.clean = clean
    }

    def setGeneratePom(boolean generatePom) {
        this.generatePom = generatePom
    }

    boolean isClean() {
        return this.clean
    }

    boolean isGeneratePom() {
        return this.generatePom
    }

    boolean isGenerate() {

        def retVal = false;

        if (this.deploy) {
            retVal = (this.label && this.repository && this.group)
        } else {
            retVal = (this.label && this.group)
        }
        return retVal
    }

    def setMavenLocation(String mvnLocation) {
        this.mvnLocation = mvnLocation
    }

    String getMavenLocation() {
        return this.mvnLocation
    }
}
