package com.PPOOII.Proyecto.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PPOOII.Proyecto.Entities.Documento;

@Repository("IDocumentoRepo")
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    Optional<Documento> findByCodigo(String codigo);

    List<Documento> findByTipoVehiculo(String tipoVehiculo);

    Page<Documento> findAll(Pageable pageable);
}
