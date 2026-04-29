package com.PPOOII.Proyecto.Entities;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "vehiculo_documento", schema = "ppooii_proyecto")
@Check(constraints = "estado IN ('Habilitado', 'Vencido', 'En Verificacion')")
public class VehiculoDocumento implements Serializable {

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
    @JoinColumn(name = "ID_DOCUMENTO", nullable = false)
    private Documento documento;

    @Column(name = "FECHA_EXPEDICION", nullable = false)
    private LocalDate fechaExpedicion;

    @Column(name = "FECHA_VENCIMIENTO", nullable = false)
    private LocalDate fechaVencimiento;

    /**
     * Habilitado | Vencido | En Verificacion
     * Estado inicial al crear un vehículo: "En Verificacion"
     */
    @Column(name = "ESTADO", nullable = false, length = 15)
    private String estado = "En Verificacion";

    /**
     * Campo para almacenar el documento PDF en BASE64
     * Tipo BLOB en la base de datos
     */
    @Lob
    @Column(name = "DOCUMENTO_BASE64", columnDefinition = "BLOB")
    private byte[] documentoBase64;

    // Constructores

    public VehiculoDocumento() {}

    public VehiculoDocumento(Vehiculo vehiculo, Documento documento,
                             LocalDate fechaExpedicion, LocalDate fechaVencimiento) {
        this.vehiculo = vehiculo;
        this.documento = documento;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = "En Verificacion";
    }

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vehiculo getVehiculo() { return vehiculo; }
    public void setVehiculo(Vehiculo vehiculo) { this.vehiculo = vehiculo; }

    public Documento getDocumento() { return documento; }
    public void setDocumento(Documento documento) { this.documento = documento; }

    public LocalDate getFechaExpedicion() { return fechaExpedicion; }
    public void setFechaExpedicion(LocalDate fechaExpedicion) { this.fechaExpedicion = fechaExpedicion; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public byte[] getDocumentoBase64() { return documentoBase64; }
    public void setDocumentoBase64(byte[] documentoBase64) { this.documentoBase64 = documentoBase64; }

    @Override
    public String toString() {
        return "VehiculoDocumento [id=" + id + ", estado=" + estado + "]";
    }
}