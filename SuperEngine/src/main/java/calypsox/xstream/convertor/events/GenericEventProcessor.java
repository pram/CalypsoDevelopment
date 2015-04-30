package calypsox.xstream.convertor.events;

import com.calypso.tk.event.PSEvent;
import com.calypso.tk.event.PSEventDomainChange;
import com.calypso.tk.service.LocalCache;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Created with IntelliJ IDEA.
 * User: xe32104
 * Date: 9/05/13
 * Time: 17:10
 */
public class GenericEventProcessor implements CalypsoEventProcessor {

    @Override
    public void createWriter(PSEvent ps, HierarchicalStreamWriter writer) {
        outputDefault(ps,writer);
    }

    protected void outputDefault(PSEvent ps, HierarchicalStreamWriter writer) {
        safeWrite("className", ps.getClassName(), writer);
        safeWrite("eventType", ps.getEventType(), writer);
        safeWrite("dataserverName", ps.getDataServerName(), writer);
        safeWrite("engineName", ps.getEngineName(), writer);
    }

    protected void safeWrite(String node, Object o, HierarchicalStreamWriter writer) {
        writer.startNode(node);
        String output = o != null ? o.toString() : "";
        writer.setValue(output);
        writer.endNode();
    }
}
