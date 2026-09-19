package com.PPOOII.Proyecto.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PPOOII.Proyecto.Config.JWTAuthConfig;
import com.PPOOII.Proyecto.Config.Model.JwtRequest;
import com.PPOOII.Proyecto.Entities.Usuario;
import com.PPOOII.Proyecto.Repository.UsuarioRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        String login = request.getUsername();
        String password = request.getPassword();

        // Find user by login only
        var usuarioOpt = usuarioRepository.findById_Login(login);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Usuario no encontrado"));
        }

        Usuario usuario = usuarioOpt.get();

        // Validate password
        if (!usuario.getPassword().equals(password)) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Contraseña incorrecta"));
        }

        // Generate JWT token
        String token = JWTAuthConfig.getJWTToken(login);

        // Return token
        Map<String, String> response = new HashMap<>();
        response.put("jwt", token);

        return ResponseEntity.ok(response);
    }
}