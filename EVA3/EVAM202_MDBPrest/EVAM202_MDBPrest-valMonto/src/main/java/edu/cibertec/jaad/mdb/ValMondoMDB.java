package edu.cibertec.jaad.mdb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@MessageDriven(
		name = "MDBTopic",
		mappedName = "jms/MDBTopic",
		activationConfig = {
				@ActivationConfigProperty( propertyName = "destinationType", propertyValue = "javax.jms.Topic" )
		}
)
public class ValMondoMDB implements MessageListener {

	private Logger LOG = LoggerFactory.getLogger(ValMondoMDB.class);
	
	@Resource(mappedName = "jms/QueueMDBCF2")
	private ConnectionFactory connectionFactory;
	private Connection connection;
	@Resource(mappedName = "jms/ResultadoQueue")
	private Destination resultadoQueue;
	
	public void onMessage(Message message) {
		ObjectMessage msg = (ObjectMessage) message;
		LOG.info("Mensaje recibido: " + msg);
		try {
			Cliente cliente = (Cliente) msg.getObject();
			LOG.info("cliente: " + cliente.getDni());
			LOG.info("nombre: " + cliente.getNombre());
			LOG.info("monto: " + cliente.getMonto());
			LOG.info("sueldo: " + cliente.getSueldo());
			
			// Bussiness Logic
			LOG.info("resultado: " + (cliente.getMonto() < cliente.getSueldo() * 4));
			enviarResultadoEvaluacion(cliente);
			
		} catch (Exception e) {
			LOG.error("Error al recibir mensaje ", e);
		}
	}
	
	public void enviarResultadoEvaluacion(Cliente cliente) throws Exception {
		Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(resultadoQueue);
		MapMessage msgResp = session.createMapMessage();
		msgResp.setBoolean("RESULTADO", (cliente.getMonto() < cliente.getSueldo() * 4));
		msgResp.setString("ORIGEN", "MONTO");
		producer.send(msgResp);
		session.close();
	}
	
	
	@PostConstruct
	public void init() {
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			LOG.info("Recursos iniciados...!!");
		} catch (Exception e) {
			LOG.error("Error al inicar el MDB", e);
		}
	}
	
	@PreDestroy
	public void release(){
		try {
			connection.close();
			LOG.info("Recursos liberados");
		} catch (Exception e) {
			LOG.error("Error al finalizar el MDB");
		}
	}

}
