package com.PPOOII.Proyecto.Services.Interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.PPOOII.Proyecto.Entities.Persona;

public interface IPersonaService {

    // CRUD
    boolean guardar(Persona persona);
    boolean actualizar(Persona persona);
    boolean eliminar(long id);
    List<Persona> consultarPersonas(Pageable pageable);

    // Busquedas
    Persona findById(long id);
    Persona findByIdentificacion(String identificacion);
    List<Persona> findByTipoPersona(String tipoPersona);
}