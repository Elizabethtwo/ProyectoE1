package com.PPOOII.Proyecto.Services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.PPOOII.Proyecto.Entities.Persona;
import com.PPOOII.Proyecto.Repository.PersonaRepository;
import com.PPOOII.Proyecto.Services.Interfaces.IPersonaService;

@Service("PersonaService")
public class PersonaServiceImpl implements IPersonaService {

    @Autowired
    @Qualifier("IPersonaRepo")
    private PersonaRepository personaRepository;

    private static final Logger logger = LogManager.getLogger(PersonaServiceImpl.class);

    @Override
    public boolean guardar(Persona persona) {
        try {
            if (persona == null) {
                logger.error("ERROR GUARDAR_PERSONA: La persona es nula.");
                return false;
            }
            personaRepository.save(persona);
            logger.info("PERSONA GUARDADA: " + persona.getIdPersona());
            return true;
        } catch (Exception e) {
            logger.error("ERROR GUARDAR_PERSONA: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Persona persona) {
        try {
            if (persona == null || persona.getIdPersona() == null) {
                logger.error("ERROR ACTUALIZAR_PERSONA: La persona o su ID es nulo.");
                return false;
            }
            if (!personaRepository.existsById(persona.getIdPersona())) {
                logger.error("ERROR ACTUALIZAR_PERSONA: No existe la persona con ID: " + persona.getIdPersona());
                return false;
            }
            personaRepository.save(persona);
            logger.info("PERSONA ACTUALIZADA: " + persona.getIdPersona());
            return true;
        } catch (Exception e) {
            logger.error("ERROR ACTUALIZAR_PERSONA: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(long id) {
        try {
            if (!personaRepository.existsById(id)) {
                logger.error("ERROR ELIMINAR_PERSONA: No existe la persona con ID: " + id);
                return false;
            }
            personaRepository.deleteById(id);
            logger.info("PERSONA ELIMINADA: " + id);
            return true;
        } catch (Exception e) {
            logger.error("ERROR ELIMINAR_PERSONA: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Persona> consultarPersonas(Pageable pageable) {
        try {
            return personaRepository.findAll(pageable).getContent();
        } catch (Exception e) {
            logger.error("ERROR CONSULTAR_PERSONAS: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Persona findById(long id) {
        try {
            return personaRepository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("ERROR FIND_BY_ID_PERSONA: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Persona findByIdentificacion(String identificacion) {
        try {
            return personaRepository.findByIdentificacion(identificacion).orElse(null);
        } catch (Exception e) {
            logger.error("ERROR FIND_BY_IDENTIFICACION: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Persona> findByTipoPersona(String tipoPersona) {
        try {
            return personaRepository.findByTipoPersona(tipoPersona);
        } catch (Exception e) {
            logger.error("ERROR FIND_BY_TIPO_PERSONA: " + e.getMessage());
            return null;
        }
    }
}