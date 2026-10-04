package com.PPOOII.Proyecto.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PPOOII.Proyecto.Entities.Persona;
import com.PPOOII.Proyecto.Entities.VehiculoConductor;

@Repository("IVehiculoConductorRepo")
public interface VehiculoConductorRepository extends JpaRepository<VehiculoConductor, Long> {

    Optional<VehiculoConductor> findByVehiculoIdAndPersonaId(Long vehiculoId, Long personaId);

    @Query("SELECT DISTINCT vc.persona FROM VehiculoConductor vc " +
           "WHERE vc.estadoConductor = 'PO' AND vc.persona.tipoPersona = 'C'")
    List<Persona> findOperableDrivers();

    @Query("SELECT vc FROM VehiculoConductor vc JOIN FETCH vc.persona " +
           "WHERE vc.vehiculo.placa = :placa")
    List<VehiculoConductor> findByVehiclePlate(@Param("placa") String placa);
}
