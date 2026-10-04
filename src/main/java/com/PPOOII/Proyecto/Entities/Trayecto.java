package com.PPOOII.Proyecto.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "trayecto")
@Check(constraints = "orden_parada >= 0")
public class Trayecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idpersona", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idvehiculo", nullable = false)
    private Vehiculo vehiculo;

    @NotBlank
    @Size(max = 50)
    @Column(name = "codigo_ruta", nullable = false, length = 50)
    private String codigoRuta;

    @NotBlank
    @Size(max = 255)
    @Column(name = "ubicacion", nullable = false, length = 255)
    private String ubicacion;

    @NotNull
    @Column(name = "orden_parada", nullable = false)
    private Integer ordenParada;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @NotBlank
    @Column(name = "login_registro", nullable = false, length = 100)
    private String loginRegistro;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }
    public String getCodigoRuta() { return codigoRuta; }
    public void setCodigoRuta(String codigoRuta) { this.codigoRuta = codigoRuta; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public Integer getOrdenParada() { return ordenParada; }
    public void setOrdenParada(Integer ordenParada) { this.ordenParada = ordenParada; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public String getLoginRegistro() { return loginRegistro; }
    public void setLoginRegistro(String loginRegistro) { this.loginRegistro = loginRegistro; }
}
