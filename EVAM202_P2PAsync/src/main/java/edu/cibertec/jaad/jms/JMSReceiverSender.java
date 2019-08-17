package edu.cibertec.jaad.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSReceiverSender implements MessageListener {
    private static final String JMS_QUEUE_IN = "jms/QueueINEval";
    private static final String JMS_QUEUE_CF = "jms/QueueCFC";
    private static final Logger LOG = LoggerFactory.getLogger(JMSReceiverSender.class);
    
    private Session session;
    
    public void start() {
        try {
            Context ctx = new InitialContext();
            ConnectionFactory factory = (ConnectionFactory)ctx.lookup(JMS_QUEUE_CF);
            Connection connection = factory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination colaIN = (Destination)ctx.lookup(JMS_QUEUE_IN);
            connection.start();
            
            String selector = "DESTINO = 'evalriesgo'";            
            MessageConsumer consumer = session.createConsumer(colaIN, selector);
            
            consumer.setMessageListener(this);
            LOG.info("Esperando por mensaje...");
        } catch (Exception ex) {
            LOG.error("Error al iniciar el lector", ex);
        }
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage msg = (ObjectMessage)message;
            Persona persona = (Persona) msg.getObject();
            LOG.info("Recibido=[" + msg + "]");
            LOG.info("DNI EVAL = " + persona.getDni());
            LOG.info("MONTO EVAL = " + persona.getMonto());
        
            String dni = persona.getDni();
        	Integer iDni = Integer.parseInt(dni);
        	boolean parImpar  = iDni%2 == 0 ? true : false; // par true - impar false
            
            // RESP:
        	String resultado = (parImpar == true ? "OK" : "NO OK");
        	MapMessage msgResp = session.createMapMessage();
        	msgResp.setString("RESPUESTA", resultado);
        	msgResp.setString("ORIGEN", "RIESGO" );
        	msgResp.setString("CORRELATION_ID", message.getJMSCorrelationID());
        	
            /// TextMessage msgResp = session.createTextMessage(resultado);
			msgResp.setJMSCorrelationID(message.getJMSCorrelationID());
			
			MessageProducer producer = session.createProducer(msg.getJMSReplyTo());
			producer.send(msgResp);
			
			producer.close();
			LOG.info("Mensaje enviado = ["+ msgResp +"]");
			
        } catch (Exception ex) {
            LOG.error("Error al recibir el mensaje", ex);
        }
    }
    
    
    
    public static void main(String[] args) {
        JMSReceiverSender rs = new JMSReceiverSender();
        rs.start();
    }
}
