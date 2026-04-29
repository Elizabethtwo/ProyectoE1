package com.PPOOII.Proyecto.Entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "vehiculo", schema = "ppooii_proyecto")
public class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TIPO_VEHICULO", nullable = false, length = 12)
    private String tipoVehiculo; // Automovil | Motocicleta

    @Column(name = "PLACA", nullable = false, unique = true, length = 6)
    private String placa;

    @Column(name = "TIPO_SERVICIO", nullable = false, length = 2)
    private String tipoServicio; // Pu | Pr

    @Column(name = "TIPO_COMBUSTIBLE", nullable = false, length = 10)
    private String tipoCombustible; 

    @Column(name = "CAPACIDAD_PASAJEROS", nullable = false)
    private int capacidadPasajeros;

    @Column(name = "COLOR", nullable = false, length = 7)
    private String color; 

    @Column(name = "MODELO", nullable = false)
    private int modelo;

    @Column(name = "MARCA", nullable = false, length = 50)
    private String marca;

    @Column(name = "LINEA", nullable = false, length = 50)
    private String linea;

    @JsonManagedReference
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehiculoDocumento> documentos;

    @JsonManagedReference("vehiculo-conductor")
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehiculoConductor> conductores;

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

    public List<VehiculoConductor> getConductores() { return conductores; }
    public void setConductores(List<VehiculoConductor> conductores) { this.conductores = conductores; }

    @Override
    public String toString() {
        return "Vehiculo [id=" + id + ", placa=" + placa + ", tipo=" + tipoVehiculo + "]";
    }
}