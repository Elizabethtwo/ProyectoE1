package com.PPOOII.Proyecto.Services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.PPOOII.Proyecto.Entities.Usuario;
import com.PPOOII.Proyecto.Entities.UsuarioPersonaId;
import com.PPOOII.Proyecto.Repository.UsuarioRepository;
import com.PPOOII.Proyecto.Services.Interfaces.IUsuarioService;

@Service("UsuarioService")
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    @Qualifier("IUsuarioRepo")
    private UsuarioRepository usuarioRepository;

    private static final Logger logger = LogManager.getLogger(UsuarioServiceImpl.class);

    @Override
    public boolean guardar(Usuario usuario) {
        try {
            if (usuario == null) {
                logger.error("ERROR GUARDAR_USUARIO: El usuario es nulo.");
                return false;
            }
            usuarioRepository.save(usuario);
            logger.info("USUARIO GUARDADO: " + usuario.getId());
            return true;
        } catch (Exception e) {
            logger.error("ERROR GUARDAR_USUARIO: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        try {
            if (usuario == null || usuario.getId() == null) {
                logger.error("ERROR ACTUALIZAR_USUARIO: El usuario o su ID es nulo.");
                return false;
            }
            if (!usuarioRepository.existsById(usuario.getId())) {
                logger.error("ERROR ACTUALIZAR_USUARIO: No existe el usuario con ID: " + usuario.getId());
                return false;
            }
            usuarioRepository.save(usuario);
            logger.info("USUARIO ACTUALIZADO: " + usuario.getId());
            return true;
        } catch (Exception e) {
            logger.error("ERROR ACTUALIZAR_USUARIO: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(UsuarioPersonaId id) {
        try {
            if (!usuarioRepository.existsById(id)) {
                logger.error("ERROR ELIMINAR_USUARIO: No existe el usuario con ID: " + id);
                return false;
            }
            usuarioRepository.deleteById(id);
            logger.info("USUARIO ELIMINADO: " + id);
            return true;
        } catch (Exception e) {
            logger.error("ERROR ELIMINAR_USUARIO: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuario> consultarUsuarios(Pageable pageable) {
        try {
            return usuarioRepository.findAll(pageable).getContent();
        } catch (Exception e) {
            logger.error("ERROR CONSULTAR_USUARIOS: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean cambiarPassword(String login, String nuevaPassword) {
        try {
            if (login == null || login.isBlank()) {
                logger.error("ERROR CAMBIAR_PASSWORD: El login es obligatorio.");
                return false;
            }
            if (nuevaPassword == null || nuevaPassword.isBlank()) {
                logger.error("ERROR CAMBIAR_PASSWORD: La nueva contraseña es obligatoria.");
                return false;
            }

            Usuario usuario = usuarioRepository.findById_Login(login).orElse(null);
            if (usuario == null) {
                logger.error("ERROR CAMBIAR_PASSWORD: No existe el usuario con login " + login);
                return false;
            }

            usuario.setPassword(nuevaPassword);
            usuarioRepository.save(usuario);
            logger.info("PASSWORD_ACTUALIZADA: " + login);
            return true;
        } catch (Exception e) {
            logger.error("ERROR CAMBIAR_PASSWORD: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String regenerarApikey(String login) {
        try {
            if (login == null || login.isBlank()) {
                logger.error("ERROR REGENERAR_APIKEY: El login es obligatorio.");
                return null;
            }

            Usuario usuario = usuarioRepository.findById_Login(login).orElse(null);
            if (usuario == null) {
                logger.error("ERROR REGENERAR_APIKEY: No existe el usuario con login " + login);
                return null;
            }

            String nuevoApikey = java.util.UUID.randomUUID().toString();
            usuario.setApikey(nuevoApikey);
            usuarioRepository.save(usuario);
            logger.info("APIKEY_REGENERADA: " + login);
            return nuevoApikey;
        } catch (Exception e) {
            logger.error("ERROR REGENERAR_APIKEY: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Usuario findById(UsuarioPersonaId id) {
        try {
            return usuarioRepository.findById(id).orElse(null);
        } catch (Exception e) {
            logger.error("ERROR FIND_BY_ID_USUARIO: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Usuario findByLogin(String login) {
        try {
            return usuarioRepository.findById_Login(login).orElse(null);
        } catch (Exception e) {
            logger.error("ERROR FIND_BY_LOGIN: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Usuario findByApikey(String apikey) {
        try {
            return usuarioRepository.findByApikey(apikey).orElse(null);
        } catch (Exception e) {
            logger.error("ERROR FIND_BY_APIKEY: " + e.getMessage());
            return null;
        }
    }
}