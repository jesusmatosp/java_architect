package edu.cibertec.jaad.mdb;

import java.io.Serializable;

public class Cliente implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String nombre;
	private Double monto;
	private Double sueldo;
	private String dni;
	
	public Cliente() { }
	
	public Cliente(String nombre, Double monto, Double sueldo, String dni) {
		this.nombre = nombre;
		this.monto = monto;
		this.sueldo = sueldo;
		this.dni = dni;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[dni = " + dni + ", nombre = " + nombre + ", monto = " + monto + ", sueldo = " + sueldo + "]";
	}
	
	

}
