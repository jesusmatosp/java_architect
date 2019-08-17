package edu.cibertec.jaad.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

public class JMSClean {
	private static final int WAITING_MSG = 2;
	private static final String JMS_QUEUE = "jms/QueueOUT";
	private static final String JMS_CONNFACT = "jms/QueueCFA";
	private static final Logger LOG = Logger.getLogger(JMSClean.class);
	public static void main(String[] args) {
		try {
			Context ctx = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory)ctx.lookup(JMS_CONNFACT);
			Connection connection = factory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Destination cola = (Destination)ctx.lookup(JMS_QUEUE);
			connection.start();
			
			//Enviando el mensaje
			MessageConsumer consumer = session.createConsumer(cola);
			boolean stop = false;
			while(!stop){
				Message msg = consumer.receive(WAITING_MSG*1000);
				LOG.info("Mensaje=[" + msg + "]");
				stop = msg == null;
			}
			LOG.info("Mensajes consumidos!!");
			
			consumer.close();
			connection.close();
			System.exit(0);
		} catch (Exception ex) {
			LOG.error("Error al enviar/recibir el mensaje", ex);
		}
	}
}
