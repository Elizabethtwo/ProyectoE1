package com.PPOOII.Proyecto.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PPOOII.Proyecto.Services.Interfaces.IUsuarioService;

@RestController
@RequestMapping("/v1")
public class UsuarioController {

    @Autowired
    @Qualifier("UsuarioService")
    private IUsuarioService usuarioService;

    @PutMapping("/usuario/{login}/password")
    public ResponseEntity<?> cambiarPassword(
            @PathVariable String login,
            @RequestBody Map<String, String> request) {

        String nuevaPassword = request.get("password");
        boolean actualizado = usuarioService.cambiarPassword(login, nuevaPassword);

        if (!actualizado) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo actualizar la contraseña"));
        }

        return ResponseEntity.ok(Map.of(
                "mensaje", "Contraseña actualizada correctamente",
                "login", login
        ));
    }

    @GetMapping("/usuario/{login}/apikey")
    public ResponseEntity<?> regenerarApikey(@PathVariable String login) {
        String nuevoApikey = usuarioService.regenerarApikey(login);

        if (nuevoApikey == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "No se pudo regenerar el APIKey"));
        }

        return ResponseEntity.ok(Map.of(
                "login", login,
                "apikey", nuevoApikey
        ));
    }
}
