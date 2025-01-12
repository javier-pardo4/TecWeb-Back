package edu.uclm.esi.listasbe.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Producto {
	@Id @Column(length = 36)
	private String id;
	@Column(length = 80, nullable = false)
	private String nombre;
	@Column(length = 80)
	private String usuario;
	private float unidadesPedidas;
	private float unidadesCompradas;

	@ManyToOne
	private Lista lista;
	
	@JsonIgnore
	public Lista getLista() {
		return lista;
	}

	public void setLista(Lista lista) {
		this.lista = lista;
	}

	public Producto() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public float getUnidadesPedidas() {
		return unidadesPedidas;
	}

	public void setUnidadesPedidas(float unidadesPedidas) {
		this.unidadesPedidas = unidadesPedidas;
	}

	public float getUnidadesCompradas() {
		return unidadesCompradas;
	}

	public void setUnidadesCompradas(float unidadesCompradas) {
		this.unidadesCompradas = unidadesCompradas;
	}
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
