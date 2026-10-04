package com.PPOOII.Proyecto.Repository;

import com.PPOOII.Proyecto.Entities.Trayecto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrayectoRepository extends JpaRepository<Trayecto, Long> {

    boolean existsByCodigoRuta(String codigoRuta);

    @Query("SELECT t FROM Trayecto t JOIN FETCH t.persona JOIN FETCH t.vehiculo "
            + "WHERE t.codigoRuta = :codigoRuta ORDER BY t.ordenParada")
    List<Trayecto> findRoute(@Param("codigoRuta") String codigoRuta);

    @Query("SELECT DISTINCT t.codigoRuta FROM Trayecto t "
            + "WHERE t.persona.identificacion = :identificacion ORDER BY t.codigoRuta")
    List<String> findRouteCodesByDriver(@Param("identificacion") String identificacion);

    @Query("SELECT DISTINCT t.codigoRuta, t.persona.identificacion, t.persona.nombres, t.persona.apellidos "
            + "FROM Trayecto t WHERE t.vehiculo.placa = :placa ORDER BY t.codigoRuta")
    List<Object[]> findRoutesAndDriversByPlate(@Param("placa") String placa);

    @Query("SELECT t FROM Trayecto t JOIN FETCH t.persona JOIN FETCH t.vehiculo "
            + "WHERE t.persona.tipoPersona <> 'C' "
            + "OR EXISTS (SELECT vc.id FROM VehiculoConductor vc WHERE vc.persona = t.persona "
            + "AND vc.vehiculo = t.vehiculo AND vc.estadoConductor = 'RO') "
            + "OR NOT EXISTS (SELECT vd.id FROM VehiculoDocumento vd WHERE vd.vehiculo = t.vehiculo) "
            + "OR EXISTS (SELECT vd.id FROM VehiculoDocumento vd WHERE vd.vehiculo = t.vehiculo "
            + "AND (vd.estado <> 'Habilitado' OR vd.fechaVencimiento < CURRENT_DATE)) "
            + "ORDER BY t.codigoRuta, t.ordenParada")
    List<Trayecto> findRoutesWithoutAuthorization();

    List<Trayecto> findByLatitudIsNullOrLongitudIsNull();
}
