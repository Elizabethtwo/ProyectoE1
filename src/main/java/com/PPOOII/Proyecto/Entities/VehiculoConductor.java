package com.PPOOII.Proyecto.Entities;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "vehiculo_conductor", schema = "ppooii_proyecto")
@Check(constraints = "estado_conductor IN ('PO', 'EA', 'RO')")
public class VehiculoConductor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VEHICULO", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERSONA", nullable = false)
    private Persona persona;

    @Column(name = "FECHA_ASOCIACION", nullable = false)
    private LocalDate fechaAsociacion;

    /**
     * PO – Puede Operar
     * EA – Espera de Aprobación
     * RO – Restringido para Operar
     */
    @Column(name = "ESTADO_CONDUCTOR", nullable = false, length = 2)
    private String estadoConductor = "EA";

    // Constructores

    public VehiculoConductor() {}

    public VehiculoConductor(Vehiculo vehiculo, Persona persona, LocalDate fechaAsociacion) {
        this.vehiculo = vehiculo;
        this.persona = persona;
        this.fechaAsociacion = fechaAsociacion;
        this.estadoConductor = "EA";
    }

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    public LocalDate getFechaAsociacion() { return fechaAsociacion; }
    public void setFechaAsociacion(LocalDate fechaAsociacion) { this.fechaAsociacion = fechaAsociacion; }

    public String getEstadoConductor() { return estadoConductor; }
    public void setEstadoConductor(String estadoConductor) { this.estadoConductor = estadoConductor; }

    @Override
    public String toString() {
        return "VehiculoConductor [id=" + id + ", estado=" + estadoConductor + "]";
    }
}