package edu.cibertec.jaad.mdb;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class MDBClient {
	private static final Logger LOG = Logger.getLogger(MDBClient.class);
	private static final String CONNECTION_FACTORY_NAME = "jms/QueueMDBCF";
	private static final String QUEUE_NAME = "jms/MDBQueue";
	
	public void sendMessage(){
		try {
			Context ctx = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory)ctx.lookup(CONNECTION_FACTORY_NAME);
			Connection connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = (Destination)ctx.lookup(QUEUE_NAME);
			
			connection.start();
			
			MessageProducer producer = session.createProducer(destination);
			Orden orden = new Orden(1, "Parihuela", new Date(), 40.0, 5.0, 35.0, "RECIBIDO", "001");
			ObjectMessage message = session.createObjectMessage(orden);
			producer.send(message);
			LOG.info("Mensaje enviado=[" + message.getObject() + "]");
			
			producer.close();
			session.close();
			connection.close();
			
			System.exit(0);
		} catch (Exception e) {
			LOG.error("Error en Sender", e);
		}
	}
	
	public static void main(String[] args) {
		MDBClient client =new MDBClient();
		client.sendMessage();
	}
}
