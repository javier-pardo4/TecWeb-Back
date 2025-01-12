package edu.uclm.esi.fakeaccountsbe.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity //Incluimos la clase en la base de datos
@Table(name = "usuario") //Cambiamos el nombre de la tabla (por defecto se crearía como User pero eso daría error al ser palabra reservada)
public class User {
	
	@Id @Column(length = 60)//especificamos la clave principal en base de datos
	private String email;
	private String pwd;
	
	@JsonIgnore @Column(length = 36)
	private String token;
	
	@JsonIgnore @Transient
	private long creationTime;
	
	//@JsonIgnore
	@Transient
	private String ip;
	private String cookie;
	
	// Nuevos campos para el pago
	@Column(name = "fecha_pago")
	private LocalDate fechaPago; // Fecha en la que el usuario hizo el pago

	@Column(name = "pagado")
	private boolean pagado; // Estado que indica si el usuario ha pagado
	
	@Column(name = "confirmado")
	private boolean confirmado = false; // Indica si el usuario ha confirmado su cuenta


	
	public boolean isConfirmado() {
		return confirmado;
	}
	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(pwd);
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;		
	}
	
	public long getCreationTime() {
		return creationTime;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getIp() {
		return ip;
	}
	public void setCookie(String fakeUserId) {
		this.cookie = fakeUserId;
	}
	public String getCookie() {
		return cookie;
	}
	public LocalDate getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(LocalDate fechaPago) {
		this.fechaPago = fechaPago;
	}
	public boolean isPagado() {
		return pagado;
	}
	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}
	
}
