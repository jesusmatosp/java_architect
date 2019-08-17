package edu.cibertec.jaad.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSSubscriber implements MessageListener{

	private static final String JMS_TOPIC = "jms/JAADTopic";
	private static final String JMS_CONNFACT = "jms/TopicCF";
	private static final Logger LOG = LoggerFactory.getLogger(JMSSubscriber.class);
	
	private String id;
	private Session session;
	
	public void start() {
		try {
			Context ctx = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory) ctx.lookup(JMS_CONNFACT);
			Connection connection = factory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination colaIN = (Destination) ctx.lookup(JMS_TOPIC);
			connection.start();
			MessageConsumer consumer = session.createConsumer(colaIN);
			consumer.setMessageListener(this);
			LOG.info("[" + id + "] Esperando por mensaje... ");
		} catch (Exception e) {
			LOG.error("Error al iniciar el lector", e);
		}
	}
	
	public JMSSubscriber(String id) {
		super();
		this.id = id;
	}

	public static void main(String[] args) {
		JMSSubscriber rs = new JMSSubscriber("A");
		rs.start();
	}

	@Override
	public void onMessage(Message message) {
		try {
			ObjectMessage msg = (ObjectMessage) message;
			LOG.info("[" + id + "]Recibido=["+msg.getObject()+ "]");
		} catch (Exception e) {
			LOG.error("Error al recibir el mensaje", e);
		}
	}

}
