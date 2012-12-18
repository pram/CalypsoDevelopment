package com.naughtyzombie.calypso.maven.mojo;

import com.naughtyzombie.calypso.maven.ui.CalypsoProcessRequest;
import com.naughtyzombie.calypso.maven.ui.CalypsoProcessSelector;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 05/12/12
 * Time: 11:10
 */

/**
 * Goal which allows Calypso process management
 *
 * @goal manage
 */
public class ManageMojo extends AbstractMojo {

    /** @component */
    private CalypsoProcessSelector cpSelector;

    /**
     * User settings use to check the interactiveMode.<br>
     * Use
     * <br><br>
     * mvn -B calypso:manage
     * <br><br>
     * or<br><br>
     * mvn --batch-mode calypso:manage
     *
     * @parameter expression="${interactiveMode}" default-value="${settings.interactiveMode}"
     * @required
     */
    private Boolean interactiveMode;

    /**
     * The process catalogs to use to build a list and let the user choose from.
     * Should be defined at compile time by the calling project
     *
     * @parameter expression="${processCatalog}" default-value="ALL"
     */
    private String processCatalog;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        CalypsoProcessRequest cpRequest = new CalypsoProcessRequest();

        if ( interactiveMode.booleanValue() ) {
            getLog().info( "Generating project in Interactive mode" );
        }
        else {
            getLog().info( "Generating project in Batch mode" );
        }

        try {
            cpSelector.selectProcess(cpRequest, interactiveMode, processCatalog);
        } catch (PrompterException e) {
            throw new MojoExecutionException("Incorrect entry", e);
        }

    }
}