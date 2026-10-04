package com.PPOOII.Proyecto.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.PPOOII.Proyecto.Entities.Vehiculo;

@Repository("IVehiculoRepo")
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    // Buscar por placa e incluir sus documentos para la respuesta de consulta.
    @Query("SELECT DISTINCT v FROM Vehiculo v " +
           "LEFT JOIN FETCH v.documentos vd " +
           "LEFT JOIN FETCH vd.documento " +
           "WHERE v.placa = :placa")
    Optional<Vehiculo> findByPlaca(@Param("placa") String placa);

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

    @Query("SELECT DISTINCT v FROM Vehiculo v JOIN FETCH v.documentos vd " +
           "LEFT JOIN FETCH vd.documento " +
           "WHERE vd.estado = 'Vencido' OR vd.fechaVencimiento < :hoy")
    List<Vehiculo> findWithExpiredDocuments(@Param("hoy") LocalDate hoy);

    @Query("SELECT DISTINCT v FROM Vehiculo v JOIN FETCH v.documentos vd " +
           "LEFT JOIN FETCH vd.documento " +
           "WHERE vd.fechaVencimiento >= :desde AND vd.fechaVencimiento <= :hasta")
    List<Vehiculo> findWithDocumentsExpiringBetween(
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    // Listado paginado
    @NonNull
    Page<Vehiculo> findAll(@NonNull Pageable pageable);
}
