package com.PPOOII.Proyecto.Controller;

import com.PPOOII.Proyecto.Entities.Trayecto;
import com.PPOOII.Proyecto.Repository.TrayectoRepository;
import com.PPOOII.Proyecto.Services.TrayectoService;
import com.PPOOII.Proyecto.Services.TrayectoService.CrearRutaRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/rutas")
public class TrayectoController {

    private final TrayectoService trayectoService;
    private final TrayectoRepository trayectoRepository;

    public TrayectoController(TrayectoService trayectoService, TrayectoRepository trayectoRepository) {
        this.trayectoService = trayectoService;
        this.trayectoRepository = trayectoRepository;
    }

    @PostMapping
    public ResponseEntity<List<TrayectoResponse>> crearRuta(
            @Valid @RequestBody CrearRutaRequest request, Authentication authentication) {
        List<TrayectoResponse> creada = trayectoService.crearRuta(request, authentication.getName())
                .stream().map(TrayectoResponse::from).toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/{codigoRuta}")
    public ResponseEntity<List<TrayectoResponse>> consultarRuta(@PathVariable String codigoRuta) {
        List<Trayecto> ruta = trayectoService.consultarRuta(codigoRuta);
        if (ruta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ruta.stream().map(TrayectoResponse::from).toList());
    }

    @GetMapping("/conductor/{identificacion}/codigos")
    public List<String> consultarRutasPorConductor(@PathVariable String identificacion) {
        return trayectoRepository.findRouteCodesByDriver(identificacion);
    }

    @GetMapping("/vehiculo/{placa}")
    public List<RutaConductorResponse> consultarRutasPorVehiculo(@PathVariable String placa) {
        return trayectoRepository.findRoutesAndDriversByPlate(placa).stream()
                .map(resultado -> new RutaConductorResponse((String) resultado[0],
                        (String) resultado[1], (String) resultado[2], (String) resultado[3]))
                .toList();
    }

    @GetMapping("/no-habilitadas")
    public List<TrayectoResponse> consultarRutasNoHabilitadas() {
        return trayectoRepository.findRoutesWithoutAuthorization().stream()
                .map(TrayectoResponse::from).toList();
    }

    public record TrayectoResponse(Long id, Long idpersona, Long idvehiculo, String codigoRuta,
                                   String ubicacion, Integer ordenParada, Double latitud,
                                   Double longitud, String loginRegistro) {
        static TrayectoResponse from(Trayecto trayecto) {
            return new TrayectoResponse(trayecto.getId(), trayecto.getPersona().getId(),
                    trayecto.getVehiculo().getId(), trayecto.getCodigoRuta(), trayecto.getUbicacion(),
                    trayecto.getOrdenParada(), trayecto.getLatitud(), trayecto.getLongitud(),
                    trayecto.getLoginRegistro());
        }
    }

    public record RutaConductorResponse(String codigoRuta, String identificacionConductor,
                                        String nombres, String apellidos) { }
}
