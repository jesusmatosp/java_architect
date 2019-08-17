package edu.cibertec.jaad.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSReceiver {

	private static Logger LOG = LoggerFactory.getLogger(JMSReceiver.class);
	private static String CONNECTION_FACTORY_NAME = "jms/QueueCF_2";
    private static String QUEUE_NAME = "jms/JAADQueue2";

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
            //Obteniendo de JNDI
            Context ctx = new InitialContext();
            ConnectionFactory cnFactory = (ConnectionFactory) ctx
                    .lookup(CONNECTION_FACTORY_NAME);
            Destination destination = (Destination)ctx.lookup(QUEUE_NAME);
            
            Connection connection = cnFactory.createConnection();
            Session session = connection
                    .createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            connection.start();
            
            MessageConsumer consumer = session.createConsumer(destination);
            
            // TextMessage message = (TextMessage)consumer.receive(10000);
            ObjectMessage message = (ObjectMessage)consumer.receive(3000);
            Profesor profesor = (Profesor)message.getObject();
            LOG.info("Profesor recibido=[" + profesor.toString()+ "]");
            consumer.close();
            session.close();
            connection.close();
        } catch (Exception ex) {
            LOG.error("Error la procesar la metadata", ex);
        } finally {
        	 System.exit(0);
        }

//		try {
//			Context ctx = new InitialContext();
//			ConnectionFactory cnFactory =
//					(ConnectionFactory) ctx.lookup(CONNECTION_FACTORY_NAME);
//			Destination destination = 
//					(Destination) ctx.lookup(QUEUE_NAME);
//			
//			Connection connection = cnFactory.createConnection();
//			Session session = connection.createSession(false,
//					Session.AUTO_ACKNOWLEDGE);
//			
//			connection.start();
//			MessageConsumer consumer = session.createConsumer(destination);
//			TextMessage message = (TextMessage) consumer.receive(2000);
//			
//			//Profesor profesorMessge = (Profesor) message;
////			ObjectMessage message = (ObjectMessage)consumer.receive(2000);
////			Profesor profesor = (Profesor) message.getObject();
////			LOG.info("Profesor recibido=[" + profesor+ "]");
//
//			
//			LOG.info("Mensaje Recibido = [ " + message + " ]");
//			//LOG.info("Profesor: " + profesorMessge.getNombre() + "."+profesorMessge.getDni());
//			
//			consumer.close();
//			session.close();
//			connection.close();
//			
//			System.exit(0);
//			
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//			LOG.error("Error al procesar la metadata", e);
//		}
		
	}

}
