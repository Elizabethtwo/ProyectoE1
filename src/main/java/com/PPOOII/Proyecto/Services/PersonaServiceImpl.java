package com.PPOOII.Proyecto.Services;

import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.PPOOII.Proyecto.Entities.Persona;
import com.PPOOII.Proyecto.Entities.Usuario;
import com.PPOOII.Proyecto.Entities.UsuarioPersonaId;
import com.PPOOII.Proyecto.Repository.PersonaRepository;
import com.PPOOII.Proyecto.Repository.UsuarioRepository;
import com.PPOOII.Proyecto.Services.Interfaces.IPersonaService;

@Service("PersonaService")
public class PersonaServiceImpl implements IPersonaService {

    @Autowired
    @Qualifier("IPersonaRepo")
    private PersonaRepository personaRepository;

    @Autowired
    @Qualifier("IUsuarioRepo")
    private UsuarioRepository usuarioRepository;

    private static final Logger logger = LogManager.getLogger(PersonaServiceImpl.class);

    @Override
    @Transactional
    public boolean guardar(Persona persona) {
        try {
            if (!esPersonaValida(persona) || persona.getId() != null
                    || personaRepository.existsByIdentificacion(persona.getIdentificacion())) {
                return false;
            }
            Persona guardada = personaRepository.save(persona);
            if ("A".equals(guardada.getTipoPersona())) {
                crearUsuarioAdministrativo(guardada);
            }
            logger.info("PERSONA GUARDADA: " + guardada.getId());
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("ERROR GUARDAR_PERSONA: " + e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean actualizar(Persona persona) {
        try {
            if (!esPersonaValida(persona) || persona.getId() == null
                    || !personaRepository.existsById(persona.getId())) {
                return false;
            }

            Usuario usuario = usuarioRepository.findById_IdPersona(persona.getId()).orElse(null);
            if ("C".equals(persona.getTipoPersona()) && usuario != null) {
                return false;
            }
            Persona actualizada = personaRepository.save(persona);
            if ("A".equals(actualizada.getTipoPersona())) {
                if (usuario == null) {
                    crearUsuarioAdministrativo(actualizada);
                } else {
                    String login = generarLogin(actualizada);
                    if (!login.equals(usuario.getId().getLogin())) {
                        String password = usuario.getPassword();
                        String apikey = usuario.getApikey();
                        usuarioRepository.delete(usuario);
                        crearUsuarioAdministrativo(actualizada, password, apikey);
                    }
                }
            }
            logger.info("PERSONA ACTUALIZADA: " + persona.getId());
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("ERROR ACTUALIZAR_PERSONA: " + e.getMessage());
            return false;
        }
    }

    private boolean esPersonaValida(Persona persona) {
        return persona != null
                && persona.getIdentificacion() != null && !persona.getIdentificacion().isBlank()
                && persona.getTipoIdentificacion() != null && "CC".equals(persona.getTipoIdentificacion())
                && persona.getNombres() != null && !persona.getNombres().isBlank()
                && persona.getApellidos() != null && !persona.getApellidos().isBlank()
                && persona.getCorreo() != null && !persona.getCorreo().isBlank()
                && persona.getTipoPersona() != null
                && ("C".equals(persona.getTipoPersona()) || "A".equals(persona.getTipoPersona()));
    }

    private String generarLogin(Persona persona) {
        return persona.getNombres().trim().substring(0, 1).toLowerCase(Locale.ROOT)
                + persona.getApellidos().trim().substring(0, 1).toLowerCase(Locale.ROOT)
                + persona.getIdentificacion().trim();
    }

    private void crearUsuarioAdministrativo(Persona persona) {
        crearUsuarioAdministrativo(persona, java.util.UUID.randomUUID().toString(),
                java.util.UUID.randomUUID().toString());
    }

    private void crearUsuarioAdministrativo(Persona persona, String password, String apikey) {
        String login = generarLogin(persona);
        if (usuarioRepository.existsById_Login(login)) {
            throw new IllegalStateException("El login generado ya está asociado a otro usuario");
        }
        Usuario usuario = new Usuario();
        usuario.setId(new UsuarioPersonaId(login, persona.getId()));
        usuario.setPersona(persona);
        usuario.setPassword(password);
        usuario.setApikey(apikey);
        usuarioRepository.save(usuario);
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