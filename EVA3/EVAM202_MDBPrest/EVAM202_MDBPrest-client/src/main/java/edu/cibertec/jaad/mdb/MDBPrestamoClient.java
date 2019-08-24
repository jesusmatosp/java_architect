package edu.cibertec.jaad.mdb;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MDBPrestamoClient {

	private static Logger LOG = LoggerFactory.getLogger(MDBPrestamoClient.class);
	private static String CONNECTION_FACTORY_NAME = "jms/QueueMDBCF2";
	private static String TOPIC_NAME = "jms/MDBTopic";
	private static final String JMS_QUEUE_OUT = "jms/ResultadoQueue";
	private static final int WAITING_MSG = 10;
	
	public String enviarMensaje(Cliente cliente, Session session, Destination destination, Destination colaOut) throws Exception {
		MessageProducer producer = session.createProducer(destination);
		ObjectMessage message = session.createObjectMessage(cliente);
		producer.send(message);
		LOG.info("Mensaje enviado=[" + message.getObject() + "]");
		
		// Respuesta::
		MessageConsumer consumer = session.createConsumer(colaOut);
		MapMessage msgResp = (MapMessage) consumer
				.receive(WAITING_MSG * 1000);
		boolean result1 = msgResp.getBoolean("RESULTADO");
		// Respuesta SBS:	
		msgResp = (MapMessage)consumer.receive(WAITING_MSG*1000);
		boolean result2 = msgResp.getBoolean("RESULTADO");
		boolean evalFinal = result1 == true && result2 == true;
		producer.close();
		return "" + (evalFinal == true ? "OK" : "NOK") + " [SBS = " + result2 + " - MONTO = " + result1 + "]" ;
	}
	
	public void sendMessage(Cliente cliente) {
		try {
			Context ctx = new InitialContext();
			ConnectionFactory connectionFactory = (ConnectionFactory) ctx.lookup(CONNECTION_FACTORY_NAME);
			Connection connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = (Destination) ctx.lookup(TOPIC_NAME);
			connection.start();
			Destination colaOut = (Destination) ctx.lookup(JMS_QUEUE_OUT); // NEW 
			LOG.info(enviarMensaje(cliente, session, destination, colaOut));
			session.close();
			connection.close();
		} catch (Exception e) {
			LOG.error("Error en Sender", e);
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		MDBPrestamoClient client = new MDBPrestamoClient();
		Cliente cliente = new Cliente("Jesus", 9500.0d, 4500d, "43781848");
		client.sendMessage(cliente);
		cliente = new Cliente("Mario", 5500.0d, 1500d, "47125845");
		client.sendMessage(cliente);
		cliente = new Cliente("Sandra", 4500.0d, 3500d, "47125841");
		client.sendMessage(cliente);
		
		System.exit(0);
	}
	
}
