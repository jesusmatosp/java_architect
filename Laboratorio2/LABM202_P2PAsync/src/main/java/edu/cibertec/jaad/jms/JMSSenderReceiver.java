package edu.cibertec.jaad.jms;

import java.util.UUID;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSSenderReceiver {

	private static final Logger LOG = LoggerFactory.getLogger(JMSSenderReceiver.class);
	private static final int WAITIN_MSG = 60;
	private static final String JMS_QUEUE_OUT = "jms/QueueOUT";
    private static final String JMS_QUEUE_IN = "jms/QueueIN";
    private static final String JMS_CONNFACT = "jms/QueueCFA";

	
	public static void main(String[] args) {
		try {
			Context ctx = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory) ctx
								.lookup(JMS_CONNFACT);
			Connection connection = factory.createConnection();
			Session session = connection
						.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Destination colaIN = (Destination) ctx.lookup(JMS_QUEUE_IN);
			Destination colaOut = (Destination) ctx.lookup(JMS_QUEUE_OUT); // NEW 
			connection.start();
			// Enviando el mensaje:
			MessageProducer producer = session.createProducer(colaIN);
			MapMessage msgReq = session.createMapMessage();
			msgReq.setString("OPERACION", "Recarga");
			msgReq.setDouble("MONTO", 35.0);
			String id = UUID.randomUUID().toString();
			msgReq.setJMSCorrelationID(id);
			msgReq.setJMSReplyTo(colaOut);
			msgReq.setStringProperty("OPERACION", "Recarga");
			producer.send(msgReq);
			// Recibe respuesta:
			String selector = "JMSCorrelationID = '" + id + "'";
			MessageConsumer consumer = session.createConsumer(colaOut, selector);
			
			LOG.info("Esperando " + WAITIN_MSG + " seg.");
			TextMessage msgResp = (TextMessage) consumer
						.receive(WAITIN_MSG*1000);
			String result = msgResp == null ? 
					"SIN RESPUESTA" : msgResp.getText();
			
			LOG.info("===> " + result);
			LOG.info("MSG= " + msgResp);
			
			producer.close();
			consumer.close();
			connection.close();
			System.exit(0);
		} catch (Exception e) {
			LOG.error("Error al enviar/Recibir el mensaje", e);
			System.exit(0);
		}
	}

}
