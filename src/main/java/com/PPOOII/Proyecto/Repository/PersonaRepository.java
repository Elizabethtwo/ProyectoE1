package com.PPOOII.Proyecto.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.PPOOII.Proyecto.Entities.Persona;

@Repository("IPersonaRepo")
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByIdentificacion(String identificacion);

    List<Persona> findByTipoPersona(String tipoPersona);

    List<Persona> findByTipoIdentificacion(String tipoIdentificacion);

    interface ConteoPorTipo {
        String getTipoPersona();
        Long getTotal();
    }

    @Query("SELECT p.tipoPersona AS tipoPersona, COUNT(p) AS total " +
           "FROM Persona p GROUP BY p.tipoPersona ORDER BY p.tipoPersona")
    List<ConteoPorTipo> contarAgrupadasPorTipo();
}