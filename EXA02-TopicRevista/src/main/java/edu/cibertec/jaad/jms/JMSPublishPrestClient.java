package edu.cibertec.jaad.jms;

import java.util.UUID;

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

public class JMSPublishPrestClient {

	private static final int WAITING_MSG = 60;
	private static final String JMS_TOPIC = "jms/JAADTopicEVAL";
	private static final String JMS_CONNFACT = "jms/TopicCFEVAL";
	private static final String JMS_QUEUE_OUT = "jms/QueueOUTTOPIC";
	
	private static final Logger LOG = LoggerFactory.getLogger(JMSPublishPrestClient.class);
	
	public static void main(String[] args) {
		try {
			Context ctx = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory) ctx.lookup(JMS_CONNFACT);
			Connection connection = factory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination topic = (Destination) ctx.lookup(JMS_TOPIC);
			Destination colaOut = (Destination) ctx.lookup(JMS_QUEUE_OUT); // NEW 
			connection.start();
			
			// Enviar mensaje:
			MessageProducer producer = session.createProducer(topic);
			ObjectMessage msgReq = session.createObjectMessage();
			Persona persona = new Persona("Jesus Matos", "43781848", 10000.0d, 3500.0d);
			
			msgReq.setObject(persona);
			String id = UUID.randomUUID().toString();
			msgReq.setJMSCorrelationID(id);
			msgReq.setJMSReplyTo(colaOut);
			msgReq.setStringProperty("OPERACION", "MONTO");
			producer.send(msgReq);
			//LOG.info("Mensaje Enviado = " + msgReq);
			
			// Respuesta::
			String selector = "JMSCorrelationID = '" + id + "'";
			MessageConsumer consumer = session.createConsumer(colaOut, selector);
			MapMessage msgResp = (MapMessage)consumer.receive(WAITING_MSG*1000);
			boolean result1 = msgResp.getBoolean("RESULTADO");
			LOG.info("respuesta:" + (result1 ? "OK" : "NOK"));
			// fin respuesta
			
			// SBS:
			msgReq = session.createObjectMessage();
			msgReq.setObject(persona);
			id = UUID.randomUUID().toString();
			msgReq.setJMSCorrelationID(id);
			msgReq.setJMSReplyTo(colaOut);
			msgReq.setStringProperty("OPERACION", "SBS");
			producer.send(msgReq);
			// LOG.info("Mensaje Enviado = " + msgReq);
			
			// Respuesta SBS:
			selector = "JMSCorrelationID = '" + id + "'";
			consumer = session.createConsumer(colaOut, selector);
			msgResp = (MapMessage)consumer.receive(WAITING_MSG*1000);
			boolean result2 = msgResp.getBoolean("RESULTADO");
			LOG.info("respuesta:" + (result2 ? "OK" : "NOK"));
			
			producer.close();
			connection.close();
			
			boolean evalFinal = result1 == true && result2 == true;
			LOG.info("DATOS: " + persona.getNombre() + " , dni: " + persona.getDni() + " monto: " + persona.getMonto() + ", sueldo: " + persona.getSueldo());
			LOG.info("" + (evalFinal == true ? "OK" : "NOK") + " [SBS = " + result2 + " - MONTO = " + result1 + "]" );
			
			System.exit(0);
		} catch (Exception e) {
			LOG.error("Error al enviar/recivir mensaje ", e);
		}


	}

}
