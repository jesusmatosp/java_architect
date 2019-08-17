package edu.cibertec.jaad.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSPublish {

	private static final int WAITING_MSG = 60;
	private static final String JMS_TOPIC = "jms/JAADTopic";
	private static final String JMS_CONNFACT = "jms/TopicCF";
	private static final Logger LOG = LoggerFactory.getLogger(JMSPublish.class);
	
	public static void main(String[] args) {
		try {
			Context ctx = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory) ctx.lookup(JMS_CONNFACT);
			Connection connection = factory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Destination topic = (Destination) ctx.lookup(JMS_TOPIC);
			connection.start();
			
			// Enviar mensaje:
			MessageProducer producer = session.createProducer(topic);
			ObjectMessage msgReq = session.createObjectMessage();
			msgReq.setObject(new Oferta("Recien Llegado", 20.0d, "Televisor"));
			msgReq.setStringProperty("OPERACION", "OFERTA");
			producer.send(msgReq);
			
			LOG.info("Mensaje Enviado = " + msgReq);
			
			producer.close();
			connection.close();
			System.exit(0);
		} catch (Exception e) {
			LOG.error("Error al enviar/recivir mensaje ", e);
		}

	}

}
