package calypsox.xstream.convertor.events;

import com.calypso.tk.event.PSEvent;

/**
 * Created with IntelliJ IDEA.
 * User: xe32104
 * Date: 9/05/13
 * Time: 17:34
 */
public class EventProcessorFactory {
    public CalypsoEventProcessor createEventProcessor(PSEvent ps) {

        CalypsoEventProcessor cep = null;

        if (ps.getClassName().equals("PSEventDomainChange")) {
            cep = new DomainChangeEventProcessor();
        } else {
            cep = new GenericEventProcessor();
        }

        return cep;
    }
}
