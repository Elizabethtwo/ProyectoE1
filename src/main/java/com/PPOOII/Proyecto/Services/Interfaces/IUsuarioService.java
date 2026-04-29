package com.PPOOII.Proyecto.Services.Interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.PPOOII.Proyecto.Entities.Usuario;
import com.PPOOII.Proyecto.Entities.UsuarioPersonaId;

public interface IUsuarioService {

    // CRUD
    boolean guardar(Usuario usuario);
    boolean actualizar(Usuario usuario);
    boolean eliminar(UsuarioPersonaId id);
    List<Usuario> consultarUsuarios(Pageable pageable);

    // Busquedas
    Usuario findById(UsuarioPersonaId id);
    Usuario findByLogin(String login);
    Usuario findByApikey(String apikey);
}