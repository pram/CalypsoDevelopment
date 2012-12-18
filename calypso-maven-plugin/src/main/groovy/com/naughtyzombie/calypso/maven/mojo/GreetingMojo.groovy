package com.naughtyzombie.calypso.maven.mojo
import org.codehaus.gmaven.mojo.GroovyMojo

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 17/12/12
 * Time: 18:15
 */

/**
 * Says "Hi" to the user... er well not really :-P.
 *
 * @goal sayhi
 */
public class GreetingMojo extends GroovyMojo {
    void execute() {
        log.info('Groovy baby!')
    }
}
