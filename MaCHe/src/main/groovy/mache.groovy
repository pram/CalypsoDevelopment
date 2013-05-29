import com.naughtyzombie.mache.Worker
import com.naughtyzombie.mache.Settings

/**
 * Created with IntelliJ IDEA.
 * User: Pram Attale
 * Date: 2012/07/23
 * Time: 10:02 AM
 */

//Entry point.
Settings settings = initialize(args)

if (settings) {
    if (settings.getTmpDir() == null || settings.getTmpDir().trim().length() == 0) {
        settings.setTmpDir(".MaCHe.temp")
    }

    final File dir = Worker.createWorkingDir(settings)
    final worker = new Worker(settings, dir)
    if (settings.isClean()) worker.cleanUp()
    worker.unzip()

    if (settings.isDeploy()) {
        worker.generateDeployScript()
    } else {
        settings.setRepository("dummy")
        worker.generateInstallScript()
    }

    if (settings.isExecute()) worker.execute()
    if (settings.isGeneratePom()) worker.generatePomFragment()
}

def initialize(args) {

    def cli = new CliBuilder(usage: 'mache.groovy [settings] <jarfile>', header: 'Options:')
    // Create the list of settings.
    cli.with {
        h longOpt: 'help', 'Show usage information'
        c longOpt: 'clean', 'Clean working directory'
        d longOpt: 'deploy', 'Generate Deploy Script otherwise default to Install script'
        l longOpt: 'label', args: 1, argName: 'labelName', 'Use the following label/version in Maven'
        r longOpt: 'repository', args: 1, argName: 'repositoryUrl', 'Use the following Maven repository'
        g longOpt: 'groupName', args: 1, argName: 'groupName', 'Add to the following group in the repository'
        t longOpt: 'tempDir', args: 1, argName: 'tempDir', 'Temp directory to for intermediate files'
        x longOpt: 'execute', 'Execute script to deploy to Maven repository'
	    p longOpt: 'pom', 'Generate dependencies pom fragment to copy into pom.xml'
    }

    def options = cli.parse(args)
    if (!options) {
        println 'Error parsing arguments'
        return
    } else if (options.h || args.size() == 0) {
        cli.usage()
        return
    } else {
        final settings = new Settings();
        if (options.l) settings.setLabel(options.l)
        if (options.r) settings.setRepository(options.r)
        if (options.g) settings.setGroup(options.g)
        if (options.t) settings.setTmpDir(options.t)
        settings.setExecute(options.x)
        settings.setClean(options.c)
        settings.setDeploy(options.d)
        if (options.arguments()[0]) settings.setJarFile(options.arguments()[0])
        if (System.env.M2_HOME) settings.setMavenLocation(System.env.M2_HOME )
	    if (options.p) settings.setGeneratePom(options.p)

        return settings
    }
}
