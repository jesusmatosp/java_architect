package edu.cibertec.jaad.mdb;

import java.io.Serializable;
import java.util.Date;

public class Orden implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer idOrden;
	private String descripcion;
	private Date fechaRegistro;
	private Double subtotal;
	private Double descuentos;
	private Double total;
	private String estado;
	private String idCliente;
	
	public Orden(Integer idOrden, String descripcion, Date fechaRegistro,
			Double subtotal, Double descuentos, Double total, String estado,
			String idCliente) {
		super();
		this.idOrden = idOrden;
		this.descripcion = descripcion;
		this.fechaRegistro = fechaRegistro;
		this.subtotal = subtotal;
		this.descuentos = descuentos;
		this.total = total;
		this.estado = estado;
		this.idCliente = idCliente;
	}
	public Orden() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getIdOrden() {
		return idOrden;
	}
	public void setIdOrden(Integer idOrden) {
		this.idOrden = idOrden;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public Double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}
	public Double getDescuentos() {
		return descuentos;
	}
	public void setDescuentos(Double descuentos) {
		this.descuentos = descuentos;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(String idCliente) {
		this.idCliente = idCliente;
	}
	@Override
	public String toString() {
		return "Orden [idOrden=" + idOrden + ", descripcion=" + descripcion
				+ ", fechaRegistro=" + fechaRegistro + ", subtotal=" + subtotal
				+ ", descuentos=" + descuentos + ", total=" + total
				+ ", estado=" + estado + ", idCliente=" + idCliente + "]";
	}

}
