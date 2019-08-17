package edu.cibertec.jaad.jms;

import java.io.Serializable;

public class Persona implements Serializable {
	
	private static final long serialVersionUID = 3701407802880877677L;
	
	private String dni;
	private Double monto;
	private Double sueldo;
	
	public Persona(String dni, Double monto, Double sueldo) {
		this.dni = dni;
		this.monto = monto;
		this.sueldo = sueldo;
	}
	
	public String getDni() {
		return dni;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public Double getMonto() {
		return monto;
	}
	public void setMonto(Double monto) {
		this.monto = monto;
	}
	public Double getSueldo() {
		return sueldo;
	}
	public void setSueldo(Double sueldo) {
		this.sueldo = sueldo;
	}
	
	

}
