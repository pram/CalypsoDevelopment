package calypsox.xstream.convertor;

import calypsox.xstream.convertor.events.CalypsoEventProcessor;
import calypsox.xstream.convertor.events.EventProcessorFactory;
import com.calypso.tk.event.PSEvent;
import com.calypso.tk.event.PSEventDomainChange;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class CalypsoEventConverter implements Converter {

    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
        PSEvent ps = (PSEvent) o;

        EventProcessorFactory epf = new EventProcessorFactory();
        CalypsoEventProcessor ep = epf.createEventProcessor(ps);
        ep.createWriter(ps,writer);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        throw new UnsupportedOperationException("The CalypsoEventConverter can only write XML");
    }

    @Override
    public boolean canConvert(Class aClass) {
        return PSEvent.class.isAssignableFrom(aClass);
    }
}