package com.naughtyzombie.calypso.maven.ui;

import org.codehaus.plexus.components.interactivity.PrompterException;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 05/12/12
 * Time: 11:51
 */
public interface CalypsoProcessSelector {

    String ROLE = CalypsoProcessSelector.class.getName();

    void selectProcess(CalypsoProcessRequest cpRequest, Boolean interactiveMode, String processCatalog) throws PrompterException;
}
