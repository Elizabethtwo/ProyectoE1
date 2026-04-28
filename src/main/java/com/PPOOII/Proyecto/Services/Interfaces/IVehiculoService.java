package com.PPOOII.Proyecto.Services.Interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.PPOOII.Proyecto.Entities.Vehiculo;
import com.PPOOII.Proyecto.Entities.VehiculoDocumento;

public interface IVehiculoService {

    // CRUD
    boolean guardar(Vehiculo vehiculo);
    boolean actualizar(Vehiculo vehiculo);
    boolean eliminar(long id);
    List<Vehiculo> consultarVehiculos(Pageable pageable);

    // Búsqueda por ID
    Vehiculo findById(long id);

    // Búsquedas especiales
    Vehiculo findByPlaca(String placa);
    List<Vehiculo> findByTipoVehiculo(String tipoVehiculo);
    List<Vehiculo> findByCodigoDocumento(String codigoDocumento);
    List<Vehiculo> findByEstadoDocumento(String estado);

    // Agregar documento a un vehículo existente
    boolean agregarDocumento(long idVehiculo, VehiculoDocumento vehiculoDocumento);
}
