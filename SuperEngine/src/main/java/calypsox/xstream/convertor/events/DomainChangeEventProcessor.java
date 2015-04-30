package calypsox.xstream.convertor.events;

import com.calypso.tk.event.PSEvent;
import com.calypso.tk.event.PSEventDomainChange;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Created with IntelliJ IDEA.
 * User: xe32104
 * Date: 9/05/13
 * Time: 17:12
 */
public class DomainChangeEventProcessor extends GenericEventProcessor {

    @Override
    public void createWriter(PSEvent ps, HierarchicalStreamWriter writer) {
        outputDefault(ps,writer);

        PSEventDomainChange psedc = (PSEventDomainChange) ps;

        safeWrite("value", psedc.getValue(), writer);
        safeWrite("action", psedc.getActionString(), writer);
        safeWrite("info", psedc.getInfo(), writer);
    }
}
