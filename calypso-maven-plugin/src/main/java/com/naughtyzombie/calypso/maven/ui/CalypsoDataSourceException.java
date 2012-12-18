package com.naughtyzombie.calypso.maven.ui;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 07/12/12
 * Time: 10:56
 */
public class CalypsoDataSourceException extends Exception {

    public CalypsoDataSourceException( Throwable throwable )
    {
        super( throwable );
    }

    public CalypsoDataSourceException(String s, Throwable throwable) {
        super (s,throwable);
    }

    public CalypsoDataSourceException(String s) {
        super (s);
    }
}
