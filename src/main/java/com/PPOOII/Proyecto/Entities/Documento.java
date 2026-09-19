package com.PPOOII.Proyecto.Entities;

import java.io.Serializable;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "documento")
@Check(constraints = "tipo_vehiculo IN ('A', 'M', 'AM') AND obligatorio IN ('RA', 'RM', 'RR')")
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20, message = "El código no puede superar 20 caracteres")
    @Column(name = "CODIGO", nullable = false, unique = true, length = 20)
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    /**
     * A  = Aplica a Automóvil
     * M  = Aplica a Motocicleta
     * AM = Aplica a ambos
     */
    @NotBlank(message = "El tipo de vehículo al que aplica es obligatorio")
    @Pattern(regexp = "A|M|AM", message = "tipoVehiculo debe ser A, M o AM")
    @Column(name = "TIPO_VEHICULO", nullable = false, length = 2)
    private String tipoVehiculo;

    /**
     * RA = Requerido Automóvil
     * RM = Requerido Motocicleta
     * RR = Requerido ambos
     */
    @NotBlank(message = "El campo obligatorio es requerido")
    @Pattern(regexp = "RA|RM|RR", message = "obligatorio debe ser RA, RM o RR")
    @Column(name = "OBLIGATORIO", nullable = false, length = 2)
    private String obligatorio;

    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
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
