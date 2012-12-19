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
    final File dir = Worker.createWorkingDir(settings)
    final worker = new Worker(settings, dir)
    worker.unzip()
    worker.generateScript();

    if (settings.isExecute()) worker.execute()
    if (settings.isGeneratePom()) worker.generatePomFragment()

    if (settings.isClean()) worker.cleanUp()
}

def initialize(args) {

    def cli = new CliBuilder(usage: 'mache.groovy [settings] <jarfile>', header: 'Options:')
    // Create the list of settings.
    cli.with {
        h longOpt: 'help', 'Show usage information'
        c longOpt: 'clean', 'Clean working directory' 
        l longOpt: 'label', args: 1, argName: 'labelName', 'Use the following label in Maven'
        r longOpt: 'repository', args: 1, argName: 'repositoryUrl', 'Use the following Maven repository'
        g longOpt: 'groupName', args: 1, argName: 'groupName', 'Add to the following group in the repository'
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
        settings.setExecute(options.x)
        settings.setClean(options.c)
        if (options.arguments()[0]) settings.setJarFile(options.arguments()[0])
        if (System.env.M2_HOME) settings.setMavenLocation(System.env.M2_HOME )
	if (options.p) settings.setGeneratePom(options.p)

        return settings
    }
}
