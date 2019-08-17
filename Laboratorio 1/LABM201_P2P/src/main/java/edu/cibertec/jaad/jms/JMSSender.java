package edu.cibertec.jaad.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSSender {
	
	private static Logger LOG = LoggerFactory.getLogger(JMSSender.class);
	private static String CONNECTION_FACTORY_NAME = "jms/QueueCF_2";
    private static String QUEUE_NAME = "jms/JAADQueue2";


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Context ctx = new InitialContext();
			ConnectionFactory cnFactory = (ConnectionFactory) ctx.lookup(CONNECTION_FACTORY_NAME);
			Destination destination = (Destination) ctx.lookup(QUEUE_NAME);
			Connection connection = cnFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			connection.start();
			MessageProducer producer = session.createProducer(destination);
			ObjectMessage message = session.createObjectMessage();
			message.setObject(new Profesor("Jorge", "0004"));
			// TextMessage message = session.createTextMessage("Hello World JMS!");
			producer.send(message);
			LOG.info("Mensaje enviado = [" + message + "]");
			producer.close();
			session.close();
			connection.close();
		} catch (Exception e) {
			LOG.error("Error al procesar la metada", e);
		} finally {
			System.exit(0);
		}
		
	}

}
