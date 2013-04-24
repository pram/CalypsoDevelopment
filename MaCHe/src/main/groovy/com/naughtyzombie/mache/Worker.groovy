package com.naughtyzombie.mache

import groovy.xml.MarkupBuilder

/**
 * Created with IntelliJ IDEA.
 * User: Pram Attale
 * Date: 2012/07/23
 * Time: 11:11 AM
 */
public class Worker {
    final String GEN_DEPLOY_SCRIPT = 'mvndeploy.script'
    final String GEN_INSTALL_SCRIPT = 'mvninstall.script'
    final String POM_FRAGMENT = 'pom.fragment'

    private Settings settings;
    private File workingDir;

    Worker(Settings settings, File workingDir) {
        this.settings = settings
        this.workingDir = workingDir;
    }

    static File createWorkingDir(Settings settings) {
        final file = new File(settings.getTmpDir())
        if (settings.isGenerate()) {
            if (file.exists() && file.isDirectory()) {
                file.deleteDir()
            }
            file.mkdir()
        }
        return file
    }

    def unzip() {
        if (this.settings.isGenerate()) {
            final builder = new AntBuilder()
            builder.unzip(src: this.settings.getJarFile(), dest: this.workingDir, overwrite: "true") {
                patternset { include(name: "**/*.jar") }
                mapper(type: "flatten")
            }
        }
    }

    def copyJars(releaseDir) {
        if (this.settings.isGenerate()) {
            final builder = new AntBuilder()

            builder.copy(toDir: this.workingDir, flatten: true) {
                fileset(dir: releaseDir) {
                    include(name:"**/*.jar")
                }
            }
        }
    }

    def cleanUp() {
        if (this.settings.isClean()) {
            new AntBuilder().delete(dir: this.workingDir)
        }
    }

    def generateDeployScript() {

        if (settings.isGenerate()) {
            final script = new File(this.workingDir, GEN_DEPLOY_SCRIPT)
            final scriptName = "mvn"
            if (System.properties['os.name'].toLowerCase().contains('windows')) {
                scriptName = scriptName +".bat"
            }
            File mvn = new File(this.settings.getMavenLocation() + "/bin", scriptName)
            final mvnPath = mvn.getAbsolutePath()
            this.workingDir.eachFile() {
                if (it.isFile() && it.name.endsWith(".jar")) {
                    script.append(mvnPath)
                    script.append(' deploy:deploy-file ')
                    script.append("-DgroupId=${this.settings.getGroup()} ")
                    script.append("-DartifactId=${it.getName().split('.jar')[0]} ")
                    script.append("-Dversion=${this.settings.getLabel()} ")
                    script.append('-Dpackaging=jar ')
                    script.append("-Dfile=${it.absolutePath} ")
                    script.append("-Durl=${this.settings.getRepository()}")
                    script.append('\n')
                }
            }
        }
    }

    def generateInstallScript() {

        if (settings.isGenerate()) {
            final script = new File(this.workingDir, GEN_INSTALL_SCRIPT)
            final scriptName = "mvn"
            if (System.properties['os.name'].toLowerCase().contains('windows')) {
                scriptName = scriptName +".bat"
            }
            File mvn = new File(this.settings.getMavenLocation() + "/bin", scriptName)
            final mvnPath = mvn.getAbsolutePath()
            this.workingDir.eachFile() {
                if (it.isFile() && it.name.endsWith(".jar")) {
                    script.append(mvnPath)
                    script.append(' install:install-file ')
                    script.append("-DgroupId=${this.settings.getGroup()} ")
                    script.append("-DartifactId=${it.getName().split('.jar')[0]} ")
                    script.append("-Dversion=${this.settings.getLabel()} ")
                    script.append('-Dpackaging=jar ')
                    script.append("-Dfile=${it.absolutePath} ")
                    script.append("-DgeneratePom=true")
                    script.append('\n')
                }
            }
        }
    }


    def execute() {
        if (!settings.getMavenLocation()) {
            println("Maven is not installed")
            return
        }
        if (settings.isExecute()) {
            final script = new File(this.workingDir, GEN_DEPLOY_SCRIPT)
            if (script.exists() && script.isFile()) {
                println "Executing " + script.getName()

                script.eachLine { line ->
                    executeLine(line)
                }
            }
        }
    }


    def executeLine(String command) {
        return executeOnShell(command, this.workingDir)
    }

    private def executeOnShell(String command, File workingDir) {
        println command
        def process = new ProcessBuilder(addShellPrefix(command))
                .directory(workingDir)
                .redirectErrorStream(true)
                .start()
        process.inputStream.eachLine {println it}
        process.waitFor();
        return process.exitValue()
    }

    private def addShellPrefix(String command) {
        def commandArray = new String[1]
        /*commandArray[0] = "sh"
        commandArray[1] = "-c"*/
        commandArray[0] = command
        return commandArray
    }

    def generatePomFragment() {
        if (settings.isGeneratePom()) {
            final script = new File(this.workingDir, GEN_DEPLOY_SCRIPT)
            if (script.exists() && script.isFile()) {
                final pomFrag = new File(this.workingDir, POM_FRAGMENT)
                pomFrag.write(' ') //clear contents of file
                script.eachLine { line ->
                    generateDependencyFragment(pomFrag, line)
                }
            } else {
                println("Execution script has not been generated")
                return
            }
        }
    }

    private def generateDependencyFragment(File pomFragment, String line) {
        def resultArray = line.split()

        def tmpGroupId
        def tmpArtifactId
        def tmpVersion

        resultArray.each {
            switch (it) {
                case ~/^-DgroupId.*$/:
                    tmpGroupId=it.split('=')[1]
                    break
                case ~/^-DartifactId.*$/:
                    tmpArtifactId=it.split('=')[1]
                    break
                case ~/^-Dversion.*$/:
                    tmpVersion=it.split('=')[1]
                    break
                default:
                    break
            }
        }

        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.dependency() {
            groupId(tmpGroupId)
            artifactId(tmpArtifactId)
            version(tmpVersion)
        }

        pomFragment.append(writer.toString())
        pomFragment.append('\n')
    }
}
