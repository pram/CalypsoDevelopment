package com.naughtyzombie.mache

/**
 * Created with IntelliJ IDEA.
 * User: Pram Attale
 * Date: 2012/07/24
 * Time: 10:24 AM
 */
class Settings {
    private String label
    private String repository
    private String group
    private boolean execute
    private boolean clean
    private boolean generatePom
    private String jarFile
    private String mvnLocation

    def setLabel(String label) {
        this.label = label
    }

    String getLabel() {
        return this.label
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
        return (this.label && this.repository && this.group)
    }

    def setMavenLocation(String mvnLocation) {
        this.mvnLocation = mvnLocation
    }

    String getMavenLocation() {
        return this.mvnLocation
    }
}
