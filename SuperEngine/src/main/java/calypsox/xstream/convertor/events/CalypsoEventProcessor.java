package calypsox.xstream.convertor.events;

import com.calypso.tk.event.PSEvent;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Created with IntelliJ IDEA.
 * User: xe32104
 * Date: 9/05/13
 * Time: 16:19
 */
public interface CalypsoEventProcessor {
    void createWriter(PSEvent ps, HierarchicalStreamWriter writer);
}
