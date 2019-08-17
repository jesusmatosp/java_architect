package edu.cibertec.jaad.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSReceiverSender implements MessageListener {

	private static final String JMS_QUEUE_IN = "jms/QueueIN";
    private static final String JMS_QUEUE_CF = "jms/QueueCFA";
    private static final Logger LOG = LoggerFactory.getLogger(JMSReceiverSender.class);
    
    private Session session;
    
    public void start() {
    	try {
			Context ctx = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory) ctx.lookup(JMS_QUEUE_CF);
			Connection connection = factory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination colaIN = (Destination) ctx.lookup(JMS_QUEUE_IN);
			connection.start();
			
			String selector = "OPERACION = 'Recarga'";
			
			// MessageConsumer consumer = session.createConsumer(colaIN);
			MessageConsumer consumer = session.createConsumer(colaIN, selector);
			
			consumer.setMessageListener(this);
			LOG.info("Esperando por mensaje...");
		} catch (Exception e) {
			LOG.error("Error al iniciar el lector", e);
		}
    }
    
	@Override
	public void onMessage(Message message) {
		try {
			MapMessage msg = (MapMessage) message;
			LOG.info("Recibido = [ " + msg + "]");
			LOG.info("Operacion=" + msg.getString("OPERACION"));
			LOG.info("Monto="+msg.getDouble("MONTO"));
			
			TextMessage msgResp = session.createTextMessage("OK");
			msgResp.setJMSCorrelationID(message.getJMSCorrelationID());
			
			MessageProducer producer = session.createProducer(msg.getJMSReplyTo());
			producer.send(msgResp);
			
			producer.close();
			LOG.info("Mensaje enviado = ["+ msgResp +"]");
			
		} catch (Exception e) {
			LOG.error("Error al recibir el mensaje", e);
		}
	}
	
	public static void main(String[] args) {
		JMSReceiverSender rs = new JMSReceiverSender();
		rs.start();
	}

}
