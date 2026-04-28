package com.PPOOII.Proyecto.Entities;

import java.io.Serializable;

import jakarta.persistence.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "documento", schema = "ppooii_proyecto")
@Check(constraints = "tipo_vehiculo IN ('A', 'M', 'AM')")
@Check(constraints = "obligatorio IN ('RA', 'RM', 'RR')")
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODIGO", nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    /**
     * A  = Aplica a Automóvil
     * M  = Aplica a Motocicleta
     * AM = Aplica a ambos
     */
    @Column(name = "TIPO_VEHICULO", nullable = false, length = 2)
    private String tipoVehiculo;

    /**
     * RA = Requerido Automóvil
     * RM = Requerido Motocicleta
     * RR = Requerido ambos
     */
    @Column(name = "OBLIGATORIO", nullable = false, length = 2)
    private String obligatorio;

    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;

    // Constructores 

    public Documento() {}

    public Documento(String codigo, String nombre, String tipoVehiculo,
                     String obligatorio, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipoVehiculo = tipoVehiculo;
        this.obligatorio = obligatorio;
        this.descripcion = descripcion;
    }

    // Getters y Setters 

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public String getObligatorio() { return obligatorio; }
    public void setObligatorio(String obligatorio) { this.obligatorio = obligatorio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return "Documento [id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + "]";
    }
}
