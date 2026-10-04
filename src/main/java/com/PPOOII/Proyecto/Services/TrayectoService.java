package com.PPOOII.Proyecto.Services;

import com.PPOOII.Proyecto.Entities.Persona;
import com.PPOOII.Proyecto.Entities.Trayecto;
import com.PPOOII.Proyecto.Entities.Vehiculo;
import com.PPOOII.Proyecto.Entities.VehiculoConductor;
import com.PPOOII.Proyecto.Entities.VehiculoDocumento;
import com.PPOOII.Proyecto.Repository.PersonaRepository;
import com.PPOOII.Proyecto.Repository.TrayectoRepository;
import com.PPOOII.Proyecto.Repository.VehiculoConductorRepository;
import com.PPOOII.Proyecto.Repository.VehiculoDocumentoRepository;
import com.PPOOII.Proyecto.Repository.VehiculoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TrayectoService {

    private final TrayectoRepository trayectoRepository;
    private final PersonaRepository personaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final VehiculoConductorRepository vehiculoConductorRepository;
    private final VehiculoDocumentoRepository vehiculoDocumentoRepository;

    public TrayectoService(TrayectoRepository trayectoRepository,
                           PersonaRepository personaRepository,
                           VehiculoRepository vehiculoRepository,
                           VehiculoConductorRepository vehiculoConductorRepository,
                           VehiculoDocumentoRepository vehiculoDocumentoRepository) {
        this.trayectoRepository = trayectoRepository;
        this.personaRepository = personaRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.vehiculoConductorRepository = vehiculoConductorRepository;
        this.vehiculoDocumentoRepository = vehiculoDocumentoRepository;
    }

    @Transactional
    public List<Trayecto> crearRuta(CrearRutaRequest request, String login) {
        if (request == null || request.idpersona() == null || request.idvehiculo() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Se requieren los identificadores del conductor y del vehículo");
        }
        Persona persona = personaRepository.findById(request.idpersona())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conductor no encontrado"));
        Vehiculo vehiculo = vehiculoRepository.findById(request.idvehiculo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado"));

        if (!"C".equals(persona.getTipoPersona())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La persona debe ser conductor");
        }
        VehiculoConductor relacion = vehiculoConductorRepository
                .findByVehiculoIdAndPersonaId(vehiculo.getId(), persona.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "El conductor no está asociado al vehículo"));
        if (!"PO".equals(relacion.getEstadoConductor())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El conductor no está habilitado para operar este vehículo");
        }

        List<VehiculoDocumento> documentos = vehiculoDocumentoRepository.findByVehiculoId(vehiculo.getId());
        if (documentos.isEmpty() || documentos.stream().anyMatch(documento ->
                !"Habilitado".equals(documento.getEstado())
                        || documento.getFechaVencimiento() == null
                        || documento.getFechaVencimiento().isBefore(LocalDate.now()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Todos los documentos asociados al vehículo deben estar habilitados y vigentes");
        }

        if (request.codigoRuta() == null || request.codigoRuta().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El código de ruta es obligatorio");
        }
        String codigoRuta = request.codigoRuta().trim();
        if (trayectoRepository.existsByCodigoRuta(codigoRuta)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El código de ruta debe ser único");
        }
        if (request.paradas() == null || request.paradas().size() < 2
                || request.paradas().size() > 7 || request.paradas().contains(null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La ruta debe tener una parada inicial, una final y hasta cinco intermedias");
        }

        List<ParadaRequest> paradas = new ArrayList<>(request.paradas());
        paradas.sort(Comparator.comparing(ParadaRequest::ordenParada,
                Comparator.nullsLast(Integer::compareTo)));
        for (int i = 0; i < paradas.size(); i++) {
            ParadaRequest parada = paradas.get(i);
            if (parada.ordenParada() == null || parada.ordenParada() != i
                    || parada.ubicacion() == null || parada.ubicacion().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Las paradas deben tener ubicación y órdenes consecutivos desde cero");
            }
        }

        List<Trayecto> trayectos = paradas.stream().map(parada -> {
            Trayecto trayecto = new Trayecto();
            trayecto.setPersona(persona);
            trayecto.setVehiculo(vehiculo);
            trayecto.setCodigoRuta(codigoRuta);
            trayecto.setUbicacion(parada.ubicacion().trim());
            trayecto.setOrdenParada(parada.ordenParada());
            trayecto.setLatitud(parada.latitud());
            trayecto.setLongitud(parada.longitud());
            trayecto.setLoginRegistro(login);
            return trayecto;
        }).toList();
        return trayectoRepository.saveAll(trayectos);
    }

    @Transactional(readOnly = true)
    public List<Trayecto> consultarRuta(String codigoRuta) {
        return trayectoRepository.findRoute(codigoRuta);
    }

    public record CrearRutaRequest(@NotNull Long idpersona, @NotNull Long idvehiculo,
                                   @NotBlank @Size(max = 50) String codigoRuta,
                                   @NotNull @Size(min = 2, max = 7) List<@Valid ParadaRequest> paradas) { }

    public record ParadaRequest(@NotNull @Min(0) Integer ordenParada,
                                @NotBlank @Size(max = 255) String ubicacion,
                                @DecimalMin("-90.0") @DecimalMax("90.0") Double latitud,
                                @DecimalMin("-180.0") @DecimalMax("180.0") Double longitud) { }
}
