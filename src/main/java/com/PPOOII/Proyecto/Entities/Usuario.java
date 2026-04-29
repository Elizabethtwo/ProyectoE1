package com.PPOOII.Proyecto.Entities;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario", schema = "ppooii_proyecto")
public class Usuario implements Serializable {

	@EmbeddedId
	private UsuarioPersonaId id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_persona", referencedColumnName = "id_persona", insertable = false, updatable = false)
	private Persona persona;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "apikey", nullable = false, unique = true, length = 255)
	private String apikey;

	// Getters y Setters

	public UsuarioPersonaId getId() {
		return id;
	}

	public void setId(UsuarioPersonaId id) {
		this.id = id;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
}
