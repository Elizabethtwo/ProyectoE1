package com.PPOOII.Proyecto.Services;

import com.PPOOII.Proyecto.Entities.Persona;
import com.PPOOII.Proyecto.Entities.Trayecto;
import com.PPOOII.Proyecto.Entities.VehiculoConductor;
import com.PPOOII.Proyecto.Entities.VehiculoDocumento;
import com.PPOOII.Proyecto.Repository.PersonaRepository;
import com.PPOOII.Proyecto.Repository.TrayectoRepository;
import com.PPOOII.Proyecto.Repository.VehiculoConductorRepository;
import com.PPOOII.Proyecto.Repository.VehiculoDocumentoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class TrayectoScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(TrayectoScheduledTasks.class);
    private final PersonaRepository personaRepository;
    private final VehiculoConductorRepository vehiculoConductorRepository;
    private final VehiculoDocumentoRepository vehiculoDocumentoRepository;
    private final TrayectoRepository trayectoRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.maps.api-key:}")
    private String googleMapsApiKey;

    public TrayectoScheduledTasks(PersonaRepository personaRepository,
                                  VehiculoConductorRepository vehiculoConductorRepository,
                                  VehiculoDocumentoRepository vehiculoDocumentoRepository,
                                  TrayectoRepository trayectoRepository) {
        this.personaRepository = personaRepository;
        this.vehiculoConductorRepository = vehiculoConductorRepository;
        this.vehiculoDocumentoRepository = vehiculoDocumentoRepository;
        this.trayectoRepository = trayectoRepository;
    }

    @Scheduled(fixedRate = 120_000, initialDelay = 120_000)
    @Transactional
    public void restringirConductoresConLicenciaVencida() {
        LocalDate hoy = LocalDate.now();
        List<VehiculoConductor> restringidos = new ArrayList<>();
        for (Persona persona : personaRepository.findByTipoPersona("C")) {
            if (persona.getFechaVigenciaLicencia() == null
                    || !persona.getFechaVigenciaLicencia().isBefore(hoy)) {
                continue;
            }
            for (VehiculoConductor relacion : vehiculoConductorRepository.findByPersona_Id(persona.getId())) {
                if (!"RO".equals(relacion.getEstadoConductor())) {
                    relacion.setEstadoConductor("RO");
                    restringidos.add(relacion);
                }
            }
        }
        if (!restringidos.isEmpty()) {
            vehiculoConductorRepository.saveAll(restringidos);
        }
    }

    @Scheduled(fixedRate = 120_000, initialDelay = 120_000)
    @Transactional
    public void vencerDocumentosDeVehiculos() {
        List<VehiculoDocumento> vencidos = vehiculoDocumentoRepository
                .findByFechaVencimientoBeforeAndEstadoNot(LocalDate.now(), "Vencido");
        vencidos.forEach(documento -> documento.setEstado("Vencido"));
        if (!vencidos.isEmpty()) {
            vehiculoDocumentoRepository.saveAll(vencidos);
        }
    }

    @Scheduled(fixedRate = 90_000, initialDelay = 90_000)
    @Transactional
    public void completarCoordenadasDeTrayectos() {
        if (googleMapsApiKey == null || googleMapsApiKey.isBlank()) {
            return;
        }
        for (Trayecto trayecto : trayectoRepository.findByLatitudIsNullOrLongitudIsNull()) {
            try {
                JsonNode respuesta = restTemplate.getForObject(UriComponentsBuilder
                        .fromUriString("https://maps.googleapis.com/maps/api/geocode/json")
                        .queryParam("address", trayecto.getUbicacion())
                        .queryParam("key", googleMapsApiKey)
                        .build().encode().toUri(), JsonNode.class);
                JsonNode ubicacion = respuesta == null ? null
                        : respuesta.path("results").path(0).path("geometry").path("location");
                if (ubicacion == null || !ubicacion.hasNonNull("lat") || !ubicacion.hasNonNull("lng")) {
                    continue;
                }
                if (trayecto.getLatitud() == null) {
                    trayecto.setLatitud(ubicacion.path("lat").asDouble());
                }
                if (trayecto.getLongitud() == null) {
                    trayecto.setLongitud(ubicacion.path("lng").asDouble());
                }
                trayectoRepository.save(trayecto);
            } catch (Exception exception) {
                logger.warn("No se pudieron obtener coordenadas para el trayecto {}: {}",
                        trayecto.getId(), exception.getMessage());
            }
        }
    }
}
