package com.PPOOII.Proyecto.Entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "vehiculo")
@Check(constraints = "tipo_vehiculo IN ('Automovil', 'Motocicleta')")
@Check(constraints = "tipo_servicio IN ('Pu', 'Pr')")
@Check(constraints = "tipo_combustible IN ('Gasolina', 'Gas', 'Disel')")
public class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotBlank(message = "El tipo de vehículo es obligatorio")
    @Pattern(regexp = "Automovil|Motocicleta", message = "Tipo de vehículo debe ser Automovil o Motocicleta")
    @Column(name = "TIPO_VEHICULO", nullable = false, length = 12)
    private String tipoVehiculo; // Automovil | Motocicleta

    @NotBlank(message = "La placa es obligatoria")
    @Column(name = "PLACA", nullable = false, unique = true, length = 6)
    private String placa;

    @NotBlank(message = "El tipo de servicio es obligatorio")
    @Pattern(regexp = "Pu|Pr", message = "Tipo de servicio debe ser Pu o Pr")
    @Column(name = "TIPO_SERVICIO", nullable = false, length = 2)
    private String tipoServicio; // Pu | Pr

    @NotBlank(message = "El tipo de combustible es obligatorio")
    @Pattern(regexp = "Gasolina|Gas|Disel", message = "Tipo de combustible debe ser Gasolina, Gas o Disel")
    @Column(name = "TIPO_COMBUSTIBLE", nullable = false, length = 10)
    private String tipoCombustible;

    @Min(value = 1, message = "La capacidad de pasajeros debe ser mayor a 0")
    @Column(name = "CAPACIDAD_PASAJEROS", nullable = false)
    private int capacidadPasajeros;

    @NotBlank(message = "El color es obligatorio")
    @Column(name = "COLOR", nullable = false, length = 7)
    private String color;

    @Min(value = 1900, message = "El modelo debe ser un año válido")
    @Column(name = "MODELO", nullable = false)
    private int modelo;

    @NotBlank(message = "La marca es obligatoria")
    @Column(name = "MARCA", nullable = false, length = 50)
    private String marca;

    @NotBlank(message = "La línea es obligatoria")
    @Column(name = "LINEA", nullable = false, length = 50)
    private String linea;

    @JsonManagedReference
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehiculoDocumento> documentos;

    // Constructores

    public Vehiculo() {}

    public Vehiculo(String tipoVehiculo, String placa, String tipoServicio,
                    String tipoCombustible, int capacidadPasajeros, String color,
                    int modelo, String marca, String linea) {
        this.tipoVehiculo = tipoVehiculo;
        this.placa = placa;
        this.tipoServicio = tipoServicio;
        this.tipoCombustible = tipoCombustible;
        this.capacidadPasajeros = capacidadPasajeros;
        this.color = color;
        this.modelo = modelo;
        this.marca = marca;
        this.linea = linea;
    }

    // ── Getters y Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public String getTipoCombustible() { return tipoCombustible; }
    public void setTipoCombustible(String tipoCombustible) { this.tipoCombustible = tipoCombustible; }

    public int getCapacidadPasajeros() { return capacidadPasajeros; }
    public void setCapacidadPasajeros(int capacidadPasajeros) { this.capacidadPasajeros = capacidadPasajeros; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getModelo() { return modelo; }
    public void setModelo(int modelo) { this.modelo = modelo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getLinea() { return linea; }
    public void setLinea(String linea) { this.linea = linea; }

    public List<VehiculoDocumento> getDocumentos() { return documentos; }
    public void setDocumentos(List<VehiculoDocumento> documentos) { this.documentos = documentos; }

    @Override
    public String toString() {
        return "Vehiculo [id=" + id + ", placa=" + placa + ", tipo=" + tipoVehiculo + "]";
    }
}