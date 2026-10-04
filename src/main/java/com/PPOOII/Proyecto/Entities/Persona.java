package com.PPOOII.Proyecto.Entities;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "persona", schema = "ppooii_proyecto")
@Check(constraints = "tipo_identificacion = 'CC' AND tipo_persona IN ('C', 'A')")
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
	@NotBlank
	@Column(name = "identificacion", nullable = false, unique = true, length = 30)
	private String identificacion;

	@NotBlank
	@Pattern(regexp = "CC")
	@Column(name = "tipo_identificacion", nullable = false, length = 5)
	private String tipoIdentificacion; // Ejemplo: CC

	@NotBlank
	@Column(name = "nombres", nullable = false, length = 100)
	private String nombres;

	@NotBlank
	@Column(name = "apellidos", nullable = false, length = 100)
	private String apellidos;

	@NotBlank
	@Email
	@Column(name = "correo", nullable = false, length = 100)
	private String correo;

	@NotBlank
	@Pattern(regexp = "C|A")
	@Column(name = "tipo_persona", nullable = false, length = 1)
	private String tipoPersona; // Ejemplo: C o A

	// Getters y Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public Persona() {}

	public Persona(Long id, String identificacion, String tipoIdentificacion, String nombres, String apellidos, String correo, String tipoPersona) {
		this.id = id;
		this.identificacion = identificacion;
		this.tipoIdentificacion = tipoIdentificacion;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.correo = correo;
		this.tipoPersona = tipoPersona;
	}

}
