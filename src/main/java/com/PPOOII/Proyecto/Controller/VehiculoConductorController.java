package com.PPOOII.Proyecto.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PPOOII.Proyecto.Services.Interfaces.IVehiculoConductorService;

@RestController
@RequestMapping("/v1")
public class VehiculoConductorController {

    @Autowired
    @Qualifier("VehiculoConductorService")
    private IVehiculoConductorService vehiculoConductorService;

    @PatchMapping("/vehiculo/{idVehiculo}/conductor/{idPersona}/estado/{estado}")
    public ResponseEntity<?> cambiarEstadoConductor(
            @PathVariable long idVehiculo,
            @PathVariable long idPersona,
            @PathVariable String estado) {

        boolean actualizado = vehiculoConductorService.cambiarEstado(idVehiculo, idPersona, estado);

        if (!actualizado) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "No se pudo actualizar el estado del conductor",
                    "estadoPermitido", "PO, EA o RO"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "mensaje", "Estado actualizado correctamente",
                "idVehiculo", idVehiculo,
                "idPersona", idPersona,
                "estado", estado.toUpperCase()
        ));
    }
}
