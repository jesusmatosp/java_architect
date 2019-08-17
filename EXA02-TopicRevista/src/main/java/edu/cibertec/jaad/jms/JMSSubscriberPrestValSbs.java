package edu.cibertec.jaad.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSSubscriberPrestValSbs implements MessageListener {

	private static final String JMS_TOPIC = "jms/JAADTopicEVAL";
	private static final String JMS_CONNFACT = "jms/TopicCFEVAL";
	private static final Logger LOG = LoggerFactory.getLogger(JMSSubscriberPrestValMonto.class);
	
	private String id;
	private Session session;
	
	public JMSSubscriberPrestValSbs(String id) {
		super();
		this.id = id;
	}
	
	public void start() {
		try {
			Context ctx = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory) ctx.lookup(JMS_CONNFACT);
			Connection connection = factory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination colaIN = (Destination) ctx.lookup(JMS_TOPIC);
			connection.start();
			String selector = "OPERACION = 'SBS'";  
			MessageConsumer consumer = session.createConsumer(colaIN, selector);
			consumer.setMessageListener(this);
			LOG.info("[" + id + "] Esperando por mensaje... ");
		} catch (Exception e) {
			LOG.error("Error al iniciar el lector", e);
		}
	}

	public static void main(String[] args) {
		JMSSubscriberPrestValSbs rs = new JMSSubscriberPrestValSbs("A");
		rs.start();
	}

	@Override
	public void onMessage(Message message) {
		try {
			ObjectMessage msg = (ObjectMessage) message;
			LOG.info("[" + id + "]Recibido=["+msg.getObject()+ "]");
			// Business Logic:
			Persona persona = (Persona) msg.getObject();
			LOG.info("Persona [name = " + persona.getNombre() + ", dni = " + persona.getDni() +"]");
			MapMessage msgResp = session.createMapMessage();
			msgResp.setBoolean("RESULTADO", Integer.parseInt(persona.getDni())%2 == 0);
			msgResp.setString("ORIGEN", "SBS");
			msgResp.setJMSCorrelationID(message.getJMSCorrelationID());
			MessageProducer producer = session.createProducer(msg.getJMSReplyTo());
			producer.send(msgResp);
			producer.close();
		} catch (Exception e) {
			LOG.error("Error al recibir el mensaje", e);
		}
	}

}
