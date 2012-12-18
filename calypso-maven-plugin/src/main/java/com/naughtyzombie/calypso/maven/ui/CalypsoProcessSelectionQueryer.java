package com.naughtyzombie.calypso.maven.ui;

import com.naughtyzombie.calypso.maven.process.CalypsoProcess;
import com.naughtyzombie.calypso.maven.process.CalypsoProcessDefinition;
import org.codehaus.plexus.components.interactivity.PrompterException;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: c935533
 * Date: 11/12/12
 * Time: 10:33
 */
public interface CalypsoProcessSelectionQueryer {
    String ROLE = CalypsoProcessSelectionQueryer.class.getName();

    CalypsoProcess selectCalypsoProcess (Map<String, List<CalypsoProcess>> processes) throws PrompterException;
    boolean confirmSelection (CalypsoProcessDefinition definition) throws PrompterException;
    CalypsoProcess selectCalypsoProcess (Map<String, List<CalypsoProcess>> processes, CalypsoProcessDefinition definition) throws PrompterException;
}
