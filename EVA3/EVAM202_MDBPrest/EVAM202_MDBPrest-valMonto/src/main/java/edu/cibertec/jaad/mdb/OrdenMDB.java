package edu.cibertec.jaad.mdb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MessageDriven(
		name = "OrdenMDB",
		mappedName = "jms/MDBQueue",
		activationConfig = {
				@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue")
		}
		)
public class OrdenMDB implements MessageListener{
	private static final Logger LOG = LoggerFactory.getLogger(OrdenMDB.class);
	@Resource(mappedName = "jms/QueueMDBCF")
	private ConnectionFactory factory;
	private Connection connection;
	
	public void onMessage(Message message) {
		ObjectMessage msg = (ObjectMessage)message;
		LOG.info("Se obtuvo el mensaje:" + msg);
		try{
			Orden orden = (Orden)msg.getObject();
			LOG.info("----------- Orden ---------");
			LOG.info("ID Cliente:" + orden.getIdCliente());
			LOG.info("Descripcion:" + orden.getDescripcion());
			LOG.info("Fecha Registro:" + orden.getFechaRegistro());
			LOG.info("Total:" + orden.getTotal());
			
		}catch(Exception ex){
			LOG.error("Error al recibir el mensaje", ex);
		}
	}
	
	@PostConstruct
	public void init(){
		try{
			connection = factory.createConnection();
			connection.start();
			LOG.info("Recursos iniciados...!!");
		}catch(Exception ex){
			LOG.error("Error al iniciar el MDB", ex);
		}
	}
	@PreDestroy
	public void release(){
		try{
			connection.close();
		}catch(Exception ex){
			LOG.error("Error al liberar los recursos");
		}
	}
}
