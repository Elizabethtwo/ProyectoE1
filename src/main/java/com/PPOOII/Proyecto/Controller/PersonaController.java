package com.PPOOII.Proyecto.Controller;

import java.util.List;

import com.PPOOII.Proyecto.Entities.Persona;
import com.PPOOII.Proyecto.Services.Interfaces.IPersonaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class PersonaController {

    private final IPersonaService personaService;

    public PersonaController(@Qualifier("PersonaService") IPersonaService personaService) {
        this.personaService = personaService;
    }

    @PostMapping("/persona")
    public ResponseEntity<?> crearPersona(@Valid @RequestBody Persona persona) {
        if (!personaService.guardar(persona)) {
            return ResponseEntity.badRequest().body("No se pudo crear la persona");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personaService.findByIdentificacion(persona.getIdentificacion()));
    }

    @GetMapping("/personas")
    public List<Persona> consultarPersonas(@NonNull Pageable pageable) {
        return personaService.consultarPersonas(pageable);
    }

    @GetMapping("/persona/id/{id}")
    public ResponseEntity<Persona> consultarPersona(@PathVariable long id) {
        Persona persona = personaService.findById(id);
        return persona == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(persona);
    }

    @PutMapping("/persona")
    public ResponseEntity<?> actualizarPersona(@Valid @RequestBody Persona persona) {
        if (!personaService.actualizar(persona)) {
            return ResponseEntity.badRequest().body("No se pudo actualizar la persona");
        }
        return ResponseEntity.ok(personaService.findById(persona.getId()));
    }
}
