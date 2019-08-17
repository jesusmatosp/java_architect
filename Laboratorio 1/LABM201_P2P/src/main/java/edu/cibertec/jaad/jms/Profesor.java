package edu.cibertec.jaad.jms;

import java.io.Serializable;

public class Profesor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String dni;
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getDni() {
		return dni;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}

	public Profesor(String nombre, String dni) {
		this.nombre = nombre;
		this.dni = dni;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Profesor: [ dni = " + dni + ", nombre = " + nombre +" ] ";
	}
	
	
	
	

}
