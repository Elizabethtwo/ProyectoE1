package com.PPOOII.Proyecto.Services.Interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.PPOOII.Proyecto.Entities.Documento;

public interface IDocumentoService {

    // CRUD
    boolean guardar(Documento documento);
    boolean actualizar(Documento documento);
    boolean eliminar(long id);
    List<Documento> consultarDocumentos(Pageable pageable);

    // Busquedas
    Documento findById(long id);
    Documento findByCodigo(String codigo);
    List<Documento> findByTipoVehiculo(String tipoVehiculo);
}
