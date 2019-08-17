package edu.cibertec.jaad.jms;

import java.io.Serializable;

public class Persona implements Serializable {

	private static final long serialVersionUID = 1L;
	private String nombre;
	private String dni;
	private Double monto;
	private Double sueldo;
	
	public Persona() { }
	
	public Persona(String nombre, String dni, Double monto, Double sueldo) {
		super();
		this.nombre = nombre;
		this.dni = dni;
		this.monto = monto;
		this.sueldo = sueldo;
	}

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

	@Override
	public String toString() {
		return "Persona [ nombre = " + nombre + ", dni = " + dni + ", sueldo = " + sueldo +" , monto = " + monto + "]";
	}
	
	
}
