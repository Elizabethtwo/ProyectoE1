package com.PPOOII.Proyecto.Entities;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UsuarioPersonaId implements Serializable {

	@Column(name = "login")
	private String login;

	@Column(name = "id_persona")
	private Long idPersona;

	public UsuarioPersonaId() {
	}

	public UsuarioPersonaId(String login, Long idPersona) {
		this.login = login;
		this.idPersona = idPersona;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UsuarioPersonaId that = (UsuarioPersonaId) o;
		if (login != null ? !login.equals(that.login) : that.login != null)
			return false;
		return idPersona != null ? idPersona.equals(that.idPersona) : that.idPersona == null;
	}

	@Override
	public int hashCode() {
		int result = login != null ? login.hashCode() : 0;
		result = 31 * result + (idPersona != null ? idPersona.hashCode() : 0);
		return result;
	}
}
