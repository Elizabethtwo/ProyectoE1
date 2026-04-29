package com.PPOOII.Proyecto.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PPOOII.Proyecto.Entities.Usuario;
import com.PPOOII.Proyecto.Entities.UsuarioPersonaId;

@Repository("IUsuarioRepo")
public interface UsuarioRepository extends JpaRepository<Usuario, UsuarioPersonaId> {

    Optional<Usuario> findById_Login(String login);

    Optional<Usuario> findByApikey(String apikey);

    boolean existsById_Login(String login);
}