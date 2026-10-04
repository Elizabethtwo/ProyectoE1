package com.PPOOII.Proyecto.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.PPOOII.Proyecto.Entities.Persona;
import com.PPOOII.Proyecto.Entities.Vehiculo;
import com.PPOOII.Proyecto.Repository.PersonaRepository;
import com.PPOOII.Proyecto.Repository.VehiculoConductorRepository;
import com.PPOOII.Proyecto.Repository.VehiculoRepository;

@RestController
@RequestMapping("/v1")
public class ConsultasPublicasController {

    private final VehiculoRepository vehiculoRepository;
    private final VehiculoConductorRepository vehiculoConductorRepository;
    private final PersonaRepository personaRepository;

    public ConsultasPublicasController(
            @Qualifier("IVehiculoRepo") VehiculoRepository vehiculoRepository,
            @Qualifier("IVehiculoConductorRepo") VehiculoConductorRepository vehiculoConductorRepository,
            @Qualifier("IPersonaRepo") PersonaRepository personaRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.vehiculoConductorRepository = vehiculoConductorRepository;
        this.personaRepository = personaRepository;
    }

    @GetMapping("/vehiculos/documentos-vencidos")
    public List<Vehiculo> consultarVehiculosConDocumentosVencidos() {
        return vehiculoRepository.findWithExpiredDocuments(LocalDate.now());
    }

    @GetMapping("/conductores/operables")
    public List<Persona> consultarConductoresQuePuedenOperar() {
        return vehiculoConductorRepository.findOperableDrivers();
    }

    @GetMapping("/vehiculos/documentos-por-vencer")
    public ResponseEntity<?> consultarVehiculosConDocumentosPorVencer(@RequestParam int dias) {
        if (dias <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "El parámetro dias debe ser mayor que cero"));
        }

        LocalDate hoy = LocalDate.now();
        return ResponseEntity.ok(vehiculoRepository.findWithDocumentsExpiringBetween(hoy, hoy.plusDays(dias)));
    }

    @GetMapping("/personas/total-por-tipo")
    public List<PersonaRepository.ConteoPorTipo> consultarTotalPersonasPorTipo() {
        return personaRepository.contarAgrupadasPorTipo();
    }
}
