package com.PPOOII.Proyecto.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PPOOII.Proyecto.Entities.VehiculoDocumento;

@Repository("IVehiculoDocumentoRepo")
public interface VehiculoDocumentoRepository extends JpaRepository<VehiculoDocumento, Long> {

    List<VehiculoDocumento> findByVehiculoId(Long idVehiculo);

    List<VehiculoDocumento> findByEstado(String estado);
}
