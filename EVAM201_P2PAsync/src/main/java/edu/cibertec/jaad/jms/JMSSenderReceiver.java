package edu.cibertec.jaad.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSSenderReceiver {

	private static final String JMS_QUEUE_OUT = "";
    private static final String JMS_QUEUE_IN = "jms/QueueINVoto";
    private static final String JMS_CONNFACT = "jms/QueueCFB";
    private static final Logger LOG = LoggerFactory.getLogger(JMSSenderReceiver.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Context ctx = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory) ctx.lookup(JMS_CONNFACT);
			
			Connection connection = factory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Destination colaIN = (Destination) ctx.lookup(JMS_QUEUE_IN);
			connection.start();
			MessageProducer producer = session.createProducer(colaIN);
			//MapMessage msgReq = session.createMapMessage();
			for(int i = 0; i<20; i++) {
				ObjectMessage msgReq = session.createObjectMessage();
				String  strVoto = Math.random() > 0.5 ? "SI" : "NO";
				Voto oVoto = new Voto(strVoto);
				msgReq.setObject(oVoto);
				producer.send(msgReq);
			}
			connection.close();
			System.exit(0);
			
		} catch (Exception e) {
			LOG.error("Error al enviar/recibir el mensaje", e);
		}
	}

}
