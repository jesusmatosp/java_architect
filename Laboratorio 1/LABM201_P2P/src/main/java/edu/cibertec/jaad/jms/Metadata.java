package edu.cibertec.jaad.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ConnectionMetaData;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Metadata {

	private static Logger LOG = LoggerFactory.getLogger(Metadata.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Context ctx = new InitialContext();
			ConnectionFactory cnFactory = (ConnectionFactory) ctx.lookup("jms/QueueCF");
			Connection connection = cnFactory.createConnection();
			ConnectionMetaData metaData = connection.getMetaData();
			
			LOG.info("JMS Version: " + metaData.getJMSMajorVersion() + "." + 
					metaData.getJMSMinorVersion());
			LOG.info("JMS Provider: " + metaData.getJMSProviderName());
			LOG.info("JMS Provider Version: " + metaData.getProviderMajorVersion() + "."
					+ metaData.getProviderMinorVersion());
			
			connection.close();
			System.exit(0);
			
		} catch (Exception e) { 
			LOG.error("Error al procesar la metadata", e);
			e.printStackTrace();
		}

	}

}
