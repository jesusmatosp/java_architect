package edu.cibertec.jaad.jms;

import java.io.Serializable;

public class Voto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String respuesta;

	public Voto(String respuesta) {
		this.respuesta = respuesta;
	}
	
	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

}
