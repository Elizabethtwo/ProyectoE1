package com.PPOOII.Proyecto.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PPOOII.Proyecto.Entities.Vehiculo;

@Repository("IVehiculoRepo")
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    // Buscar por placa
    Optional<Vehiculo> findByPlaca(String placa);

    // Buscar por tipo de vehículo
    List<Vehiculo> findByTipoVehiculo(String tipoVehiculo);

    // Buscar vehiculos que tengan un tipo de documento en comun
    @Query("SELECT DISTINCT v FROM Vehiculo v " +
           "JOIN v.documentos vd " +
           "WHERE vd.documento.codigo = :codigoDocumento")
    List<Vehiculo> findByCodigoDocumento(@Param("codigoDocumento") String codigoDocumento);

    // Buscar vehiculos según el estado del documento asociado
    @Query("SELECT DISTINCT v FROM Vehiculo v " +
           "JOIN v.documentos vd " +
           "WHERE vd.estado = :estado")
    List<Vehiculo> findByEstadoDocumento(@Param("estado") String estado);

    // Listado paginado
    Page<Vehiculo> findAll(Pageable pageable);
}
