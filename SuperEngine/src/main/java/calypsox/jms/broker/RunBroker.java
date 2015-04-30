package calypsox.jms.broker;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * User: xe32104
 * Date: 21/03/13
 * Time: 12:20
 */
public class RunBroker {
    public static void main(String[] args) throws Exception {
        BrokerService broker = BrokerFactory.createBroker(new URI("xbean:datauploader-mq.xml"));
        broker.setUseJmx(true);

        broker.start();

        Object lock = new Object();
        synchronized (lock) {
            lock.wait();
        }
    }
}
