package edu.cibertec.jaad.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JMSReceiverSender implements MessageListener {
    private static final String JMS_QUEUE_IN = "jms/QueueINVoto";
    private static final String JMS_QUEUE_CF = "jms/QueueCFB";
    private static final Logger LOG = LoggerFactory
            .getLogger(JMSReceiverSender.class);
    
    private Session session;
    private Integer countYES = 0;
    private Integer countNO = 0;
    
    public void start() {
        try {
            Context ctx = new InitialContext();
            ConnectionFactory factory = (ConnectionFactory)ctx
                    .lookup(JMS_QUEUE_CF);
            Connection connection = factory.createConnection();
            session = connection
                    .createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination colaIN = (Destination)ctx.lookup(JMS_QUEUE_IN);
            connection.start();
            MessageConsumer consumer = session
                    .createConsumer(colaIN);
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
            LOG.info("Recibido=[" + msg + "]");
            Voto voto = (Voto) msg.getObject();
            LOG.info("VOTO: " + voto.getRespuesta());

            if(voto.getRespuesta().equalsIgnoreCase("SI"))
            	countYES++;
            
            if(voto.getRespuesta().equalsIgnoreCase("NO"))
            	countNO++;
            LOG.info("TOTAL SI: "  + countYES);
            LOG.info("TOTAL NO: "  + countNO);
        } catch (Exception ex) {
            LOG.error("Error al recibir el mensaje", ex);
        }
    }
    public static void main(String[] args) {
        JMSReceiverSender rs = new JMSReceiverSender();
        rs.start();
    }
}
