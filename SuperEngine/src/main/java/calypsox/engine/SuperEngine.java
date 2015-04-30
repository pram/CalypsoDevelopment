package calypsox.engine;

import com.calypso.engine.Engine;
import com.calypso.tk.core.Log;
import com.calypso.tk.event.PSEvent;
import com.calypso.tk.event.PSEventDomainChange;
import com.calypso.tk.service.DSConnection;
import com.calypso.tk.util.ConnectException;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.config.ConfigurationException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.config.spring.SpringXmlConfigurationBuilder;
import org.mule.context.DefaultMuleContextFactory;

import javax.jms.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA.
 * User: pramithattale
 * Date: 05/03/2013
 * Time: 10:57
 */
public class SuperEngine extends Engine {

    private static ActiveMQConnectionFactory connectionFactory;
    private static PooledConnectionFactory pooledConnectionFactory;
    private static Connection connection;
    private Session session;
    private static Destination destination;
    private static MessageProducer producer;
    private static boolean transacted = false;

    public SuperEngine(DSConnection dsCon, String hostName, int port) throws Exception {
        super(dsCon, hostName, port);
    }

    @Override
    public boolean start() throws ConnectException {
        try {
            Log.info(this, "Starting Embedded Broker");
            //Start the Embedded JMS Broker
            BrokerService broker = BrokerFactory.createBroker(new URI("xbean:datauploader-mq.xml"));
            broker.setUseJmx(true);

            broker.start();
            Log.info(this, "Embedded Broker Started");

            Log.info(this, "Starting Embedded Mule");
            //Start Mule
            DefaultMuleContextFactory muleContextFactory = new DefaultMuleContextFactory();
            SpringXmlConfigurationBuilder configBuilder = new SpringXmlConfigurationBuilder("mule-config.xml");
            MuleContext muleContext = muleContextFactory.createMuleContext(configBuilder);

            muleContext.start();
            Log.info(this, "Embedded Mule Started");

            //Start the client to post events
            connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);
            pooledConnectionFactory = new PooledConnectionFactory(connectionFactory);
            pooledConnectionFactory.setMaxConnections(10);
            pooledConnectionFactory.setIdleTimeout(0);
            connection = pooledConnectionFactory.createConnection();
            connection.start();
            Log.info(this, "JMS Connection Started");
            session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
            Log.info(this, "JMS Session Started");
            destination = session.createQueue("super-engine-embedded");

            Log.info(this, "JMS MessageProducer Started");
        } catch (Exception e) {
            Log.error("SuperEngine", e);
        }

        return super.start();
    }

    @Override
    public boolean start(boolean dobatch) throws ConnectException {
        return start();
    }

    @Override
    public boolean process(PSEvent psEvent) {
        boolean retVal = false;
        try {
            producer = session.createProducer(null);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            ObjectMessage message = session.createObjectMessage(psEvent);
            producer.send(destination, message);
            this.getDS().getRemoteTrade().eventProcessed(psEvent.getId(), getEngineName());
            retVal = true;
            producer.close();
        } catch (JMSException e) {
            Log.error("SuperEngine", e);
        } catch (RemoteException e) {
            Log.error("SuperEngine", e);
        }
        return retVal;
    }

    @Override
    public void processDomainChange(PSEventDomainChange ad) {
        super.processDomainChange(ad);
        process(ad);
    }

    @Override
    public String getEngineName() {
        return "SuperEngine";
    }


}
