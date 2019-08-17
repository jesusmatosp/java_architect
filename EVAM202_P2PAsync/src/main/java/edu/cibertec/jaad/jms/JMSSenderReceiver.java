package edu.cibertec.jaad.jms;

import java.util.Scanner;
import java.util.UUID;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSSenderReceiver {

	private static final int WAITIN_MSG = 60;
	private static final String JMS_QUEUE_OUT = "jms/QueueOUTEval";
    private static final String JMS_QUEUE_IN = "jms/QueueINEval";
    private static final String JMS_CONNFACT = "jms/QueueCFC";
    private static final Logger LOG = LoggerFactory.getLogger(JMSSenderReceiver.class);
	
    public static String procesarSolicitudCredito(Persona persona, Session session, Destination colaIN, Destination colaOut) throws Exception {
    	MessageProducer producer = session.createProducer(colaIN); 
		ObjectMessage msgReq = session.createObjectMessage(persona);
		String id = UUID.randomUUID().toString();
		msgReq.setJMSCorrelationID(id);
		msgReq.setJMSReplyTo(colaOut);
		msgReq.setStringProperty("DESTINO", "evalriesgo");
		producer.send(msgReq);
		boolean resultFinalEvalRi = false;
		boolean resultFinalEvalMo = false;
		String origen = "";
		String correlation_id = "";
		
		// Recibe respuesta 1:
		String selector = "JMSCorrelationID = '" + id + "'";
		MessageConsumer consumer = session.createConsumer(colaOut, selector);
	// 	LOG.info("Esperando " + WAITIN_MSG + " seg.");
		MapMessage msgResp = (MapMessage)consumer.receive(WAITIN_MSG*1000);
		String result = msgResp.getString("RESPUESTA");
		if(result.equalsIgnoreCase("OK"))
			resultFinalEvalRi = true;
		
		origen += msgResp.getString("ORIGEN") + " = " + result + ",";
		correlation_id += msgResp.getString("CORRELATION_ID");
		
		// Enviando para el cliente 2:
		msgReq = session.createObjectMessage(persona);
		id = UUID.randomUUID().toString();
		msgReq.setJMSCorrelationID(id);
		msgReq.setJMSReplyTo(colaOut);
		msgReq.setStringProperty("DESTINO", "evalmonto");
		producer.send(msgReq);
		
		// Recibe respuesta 2:
		selector = "JMSCorrelationID = '" + id + "'";
		consumer = session.createConsumer(colaOut, selector);
		// LOG.info("Esperando " + WAITIN_MSG + " seg.");
		
		msgResp = (MapMessage) consumer.receive(WAITIN_MSG*1000);
		
		result = msgResp.getString("RESPUESTA");
		
		
		if(result.equalsIgnoreCase("OK"))
			resultFinalEvalMo = true;
		
		origen += msgResp.getString("ORIGEN") + " = " + result + ",";
		correlation_id += msgResp.getString("CORRELATION_ID");
		
		String rFinal = "";
		if(resultFinalEvalRi && resultFinalEvalMo)
			rFinal = "OK";
		else
			rFinal = "NO OK";
		
		producer.close();
		consumer.close();
		
    	return "DNI: "+ persona.getDni() + "-> "+ rFinal + "["+origen+"]" + ", correlations id's: [" + correlation_id + "]";
    }

    
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Context ctx = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory) ctx.lookup(JMS_CONNFACT);
			
			Connection connection = factory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Destination colaIN = (Destination) ctx.lookup(JMS_QUEUE_IN);
			Destination colaOut = (Destination) ctx.lookup(JMS_QUEUE_OUT); // NEW 
			connection.start();
			// Enviando el mensaje:
			Persona persona = null;
			
			
			Scanner sc = new Scanner(System.in);
			
			int cantidad ;
			System.out.println("Ingrese la cantidad de personas a ingresar: ");
			cantidad = sc.nextInt();
			
			int contador = 0;
			while(contador < cantidad) {
				String dni;
				System.out.print("Introduzca un número DNI: ");
				dni = sc.next();
				
				Double monto;
				System.out.print("Introduzca el monto solicitado: ");
				monto = sc.nextDouble();
				
				Double sueldo;
				System.out.print("Introduzca su sueldo: ");
				sueldo = sc.nextDouble();
				
				persona = new Persona(dni, monto, sueldo);
				LOG.info("" + procesarSolicitudCredito(persona, session, colaIN, colaOut));
				contador++;
			}
			
			
			
			
			
//			persona = new Persona("43781848", 10000.0, 5000.0);
//			LOG.info("" + procesarSolicitudCredito(persona, session, colaIN, colaOut));
//			
//			persona = new Persona("74865239", 9000.0, 3200.0);
//			LOG.info("" + procesarSolicitudCredito(persona, session, colaIN, colaOut));
//
//			persona = new Persona("25874169", 8000.0, 1200.0);
//			LOG.info("" + procesarSolicitudCredito(persona, session, colaIN, colaOut));
//			
//			persona = new Persona("47125868", 25000.0, 7200.0);
//			LOG.info("" + procesarSolicitudCredito(persona, session, colaIN, colaOut));
//			
//			persona = new Persona("12125874", 19000.0, 12000.0);
//			LOG.info("" + procesarSolicitudCredito(persona, session, colaIN, colaOut));
//			 

			connection.close();
			System.exit(0);
			
		} catch (Exception e) {
			LOG.error("Error al enviar/recibir el mensaje", e);
		}
	}

}
