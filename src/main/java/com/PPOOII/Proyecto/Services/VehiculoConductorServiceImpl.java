package com.PPOOII.Proyecto.Services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.PPOOII.Proyecto.Entities.Persona;
import com.PPOOII.Proyecto.Entities.Vehiculo;
import com.PPOOII.Proyecto.Entities.VehiculoConductor;
import com.PPOOII.Proyecto.Repository.PersonaRepository;
import com.PPOOII.Proyecto.Repository.VehiculoConductorRepository;
import com.PPOOII.Proyecto.Repository.VehiculoRepository;
import com.PPOOII.Proyecto.Services.Interfaces.IVehiculoConductorService;

@Service("VehiculoConductorService")
public class VehiculoConductorServiceImpl implements IVehiculoConductorService {

    private static final Logger logger = LogManager.getLogger(VehiculoConductorServiceImpl.class);
    private static final Set<String> ESTADOS_VALIDOS = Set.of("PO", "EA", "RO");

    @Autowired
    @Qualifier("IVehiculoConductorRepo")
    private VehiculoConductorRepository vehiculoConductorRepository;

    @Autowired
    @Qualifier("IPersonaRepo")
    private PersonaRepository personaRepository;

    @Autowired
    @Qualifier("IVehiculoRepo")
    private VehiculoRepository vehiculoRepository;

    @Override
    public boolean cambiarEstado(long idVehiculo, long idPersona, String nuevoEstado) {
        try {
            if (nuevoEstado == null || nuevoEstado.isBlank()) {
                logger.error("ERROR CAMBIAR_ESTADO_CONDUCTOR: El estado es obligatorio.");
                return false;
            }

            String estadoNormalizado = nuevoEstado.trim().toUpperCase();
            if (!ESTADOS_VALIDOS.contains(estadoNormalizado)) {
                logger.error("ERROR CAMBIAR_ESTADO_CONDUCTOR: Estado inválido: " + nuevoEstado);
                return false;
            }

            VehiculoConductor relacion = vehiculoConductorRepository
                    .findByVehiculoIdAndPersonaId(idVehiculo, idPersona)
                    .orElse(null);

            if (relacion == null || relacion.getPersona() == null
                                || !"C".equals(relacion.getPersona().getTipoPersona())) {
                logger.error("ERROR CAMBIAR_ESTADO_CONDUCTOR: No existe una relación válida con un conductor");
                return false;
            }

            relacion.setEstadoConductor(estadoNormalizado);
            vehiculoConductorRepository.save(relacion);
            logger.info("ESTADO_CONDUCTOR_ACTUALIZADO: vehiculo=" + idVehiculo + ", persona=" + idPersona + ", estado=" + estadoNormalizado);
            return true;
        } catch (Exception e) {
            logger.error("ERROR CAMBIAR_ESTADO_CONDUCTOR: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean asociarVehiculos(long idPersona, List<Long> idsVehiculos) {
        try {
            if (idsVehiculos == null || idsVehiculos.isEmpty()) {
                logger.error("ERROR ASOCIAR_VEHICULOS: Debe enviar al menos un vehículo.");
                return false;
            }

            Persona persona = personaRepository.findById(idPersona).orElse(null);
            if (persona == null || !"C".equals(persona.getTipoPersona())) {
                logger.error("ERROR ASOCIAR_VEHICULOS: La persona no existe o no es conductor");
                return false;
            }

            Map<Long, Vehiculo> vehiculos = new HashMap<>();
            for (Long idVehiculo : idsVehiculos) {
                if (idVehiculo == null || idVehiculo <= 0) {
                    logger.error("ERROR ASOCIAR_VEHICULOS: ID de vehículo inválido: " + idVehiculo);
                    return false;
                }
                Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo).orElse(null);
                if (vehiculo == null) {
                    logger.error("ERROR ASOCIAR_VEHICULOS: No existe vehículo con ID " + idVehiculo);
                    return false;
                }
                vehiculos.put(idVehiculo, vehiculo);
            }

            for (Long idVehiculo : idsVehiculos) {
                if (vehiculoConductorRepository.findByVehiculoIdAndPersonaId(idVehiculo, idPersona).isPresent()) {
                    continue;
                }

                VehiculoConductor relacion = new VehiculoConductor();
                relacion.setVehiculo(vehiculos.get(idVehiculo));
                relacion.setPersona(persona);
                relacion.setFechaAsociacion(LocalDate.now());
                relacion.setEstadoConductor("EA");
                vehiculoConductorRepository.save(relacion);
            }

            logger.info("VEHICULOS_ASOCIADOS: persona=" + idPersona + ", cantidad=" + idsVehiculos.size());
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("ERROR ASOCIAR_VEHICULOS: " + e.getMessage(), e);
            return false;
        }
    }
}
