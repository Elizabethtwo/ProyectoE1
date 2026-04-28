package com.PPOOII.Proyecto.Services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.PPOOII.Proyecto.Entities.Documento;
import com.PPOOII.Proyecto.Entities.Vehiculo;
import com.PPOOII.Proyecto.Entities.VehiculoDocumento;
import com.PPOOII.Proyecto.Repository.DocumentoRepository;
import com.PPOOII.Proyecto.Repository.VehiculoDocumentoRepository;
import com.PPOOII.Proyecto.Repository.VehiculoRepository;
import com.PPOOII.Proyecto.Services.Interfaces.IVehiculoService;

@Service("VehiculoService")
public class VehiculoServiceImpl implements IVehiculoService {

    @Autowired
    @Qualifier("IVehiculoRepo")
    private VehiculoRepository vehiculoRepository;

    @Autowired
    @Qualifier("IVehiculoDocumentoRepo")
    private VehiculoDocumentoRepository vdRepository;

    @Autowired
    @Qualifier("IDocumentoRepo")
    private DocumentoRepository documentoRepository;

    private static final Logger logger = LogManager.getLogger(VehiculoServiceImpl.class);

    // Validacion de formato de placa
    private boolean validarFormatoPlaca(String placa, String tipoVehiculo) {
        if (placa == null || tipoVehiculo == null) {
            return false;
        }
        String placaUpper = placa.toUpperCase();
        
        if ("Automovil".equals(tipoVehiculo)) {
            return placaUpper.matches("^[A-Z]{3}[0-9]{3}$");
        } else if ("Motocicleta".equals(tipoVehiculo)) {
            return placaUpper.matches("^[A-Z]{3}[0-9]{2}[A-Z]$");
        }
        return false;
    }

    // Validacion de compatibilidad documento-vehiculo
    private boolean documentoAplicaAVehiculo(String tipoVehiculoDoc, String tipoVehiculo) {
        if ("AM".equals(tipoVehiculoDoc)) {
            return true;
        }
        if ("A".equals(tipoVehiculoDoc) && "Automovil".equals(tipoVehiculo)) {
            return true;
        }
        if ("M".equals(tipoVehiculoDoc) && "Motocicleta".equals(tipoVehiculo)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean guardar(Vehiculo vehiculo) {
        try {
            if (vehiculo == null) {
                logger.error("ERROR GUARDAR_VEHICULO: El vehículo es nulo.");
                return false;
            }
            
            // Validar formato de placa
            if (!validarFormatoPlaca(vehiculo.getPlaca(), vehiculo.getTipoVehiculo())) {
                logger.error("ERROR GUARDAR_VEHICULO: Formato de placa inválido para " + 
                           vehiculo.getTipoVehiculo() + ". Placa: " + vehiculo.getPlaca());
                return false;
            }
            vehiculo.setPlaca(vehiculo.getPlaca().toUpperCase());
            
            // Validar que tenga al menos un documento
            if (vehiculo.getDocumentos() == null || vehiculo.getDocumentos().isEmpty()) {
                logger.error("ERROR GUARDAR_VEHICULO: No se puede crear un vehículo sin documentos.");
                return false;
            }
            
            // Buscar cada documento en BD y validarc
            for (VehiculoDocumento vd : vehiculo.getDocumentos()) {
                if (vd.getDocumento() == null || vd.getDocumento().getId() == null) {
                    logger.error("ERROR GUARDAR_VEHICULO: Documento sin ID.");
                    return false;
                }
                
                // BUSCAR EL DOCUMENTO EN LA BASE DE DATOS
                Long docId = vd.getDocumento().getId();
                Documento docBD = documentoRepository.findById(docId).orElse(null);
                
                if (docBD == null) {
                    logger.error("ERROR GUARDAR_VEHICULO: No existe documento con ID " + docId);
                    return false;
                }
                
                // Usar el documento de la BD para la validación y asignacion
                vd.setDocumento(docBD);
                
                // Validar que aplique al tipo de vehiculo
                if (!documentoAplicaAVehiculo(docBD.getTipoVehiculo(), vehiculo.getTipoVehiculo())) {
                    logger.error("ERROR GUARDAR_VEHICULO: El documento " + 
                               docBD.getCodigo() + " no aplica para " + vehiculo.getTipoVehiculo());
                    return false;
                }
            }
            
            // Forzar estado inicial y asignar vehiculo
            for (VehiculoDocumento vd : vehiculo.getDocumentos()) {
                vd.setEstado("En Verificacion");
                vd.setVehiculo(vehiculo);
            }
            
            vehiculoRepository.save(vehiculo);
            logger.info("Vehículo guardado exitosamente con placa: " + vehiculo.getPlaca());
            return true;
            
        } catch (Exception e) {
            logger.error("ERROR GUARDAR_VEHICULO: " + e.getMessage(), e);
            return false;
        }
    }

    // resto de métodos (actualizar, eliminar, consultas)

    @Override
    public boolean actualizar(Vehiculo vehiculo) {
        try {
            if (vehiculo == null || vehiculo.getId() == null || vehiculo.getId() == 0) {
                logger.error("ERROR ACTUALIZAR_VEHICULO: El vehículo es nulo o el ID es 0.");
                return false;
            }
            
            if (vehiculo.getPlaca() != null && vehiculo.getTipoVehiculo() != null) {
                if (!validarFormatoPlaca(vehiculo.getPlaca(), vehiculo.getTipoVehiculo())) {
                    logger.error("ERROR ACTUALIZAR_VEHICULO: Formato de placa inválido.");
                    return false;
                }
                vehiculo.setPlaca(vehiculo.getPlaca().toUpperCase());
            }
            
            vehiculoRepository.save(vehiculo);
            logger.info("Vehículo actualizado exitosamente con ID: " + vehiculo.getId());
            return true;
            
        } catch (Exception e) {
            logger.error("ERROR ACTUALIZAR_VEHICULO: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(long id) {
        try {
            if (id == 0) {
                logger.error("ERROR ELIMINAR_VEHICULO: El ID es 0.");
                return false;
            }
            Vehiculo vehiculo = vehiculoRepository.findById(id).orElse(null);
            if (vehiculo == null) {
                logger.error("ERROR ELIMINAR_VEHICULO: No existe vehículo con ID " + id);
                return false;
            }
            vehiculoRepository.delete(vehiculo);
            logger.info("Vehículo eliminado exitosamente con ID: " + id);
            return true;
        } catch (Exception e) {
            logger.error("ERROR ELIMINAR_VEHICULO: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Vehiculo> consultarVehiculos(Pageable pageable) {
        return vehiculoRepository.findAll(pageable).getContent();
    }

    @Override
    public Vehiculo findById(long id) {
        return vehiculoRepository.findById(id).orElse(null);
    }

    @Override
    public Vehiculo findByPlaca(String placa) {
        if (placa != null) {
            placa = placa.toUpperCase();
        }
        return vehiculoRepository.findByPlaca(placa).orElse(null);
    }

    @Override
    public List<Vehiculo> findByTipoVehiculo(String tipoVehiculo) {
        return vehiculoRepository.findByTipoVehiculo(tipoVehiculo);
    }

    @Override
    public List<Vehiculo> findByCodigoDocumento(String codigoDocumento) {
        return vehiculoRepository.findByCodigoDocumento(codigoDocumento);
    }

    @Override
    public List<Vehiculo> findByEstadoDocumento(String estado) {
        return vehiculoRepository.findByEstadoDocumento(estado);
    }

    @Override
    public boolean agregarDocumento(long idVehiculo, VehiculoDocumento vehiculoDocumento) {
        try {
            Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo).orElse(null);
            if (vehiculo == null) {
                logger.error("ERROR AGREGAR_DOCUMENTO: No existe vehículo con ID " + idVehiculo);
                return false;
            }
            
            if (vehiculoDocumento.getDocumento() == null || vehiculoDocumento.getDocumento().getId() == null) {
                logger.error("ERROR AGREGAR_DOCUMENTO: El documento es nulo o sin ID.");
                return false;
            }
            
            // BUSCAR DOCUMENTO EN BD
            Long docId = vehiculoDocumento.getDocumento().getId();
            Documento docBD = documentoRepository.findById(docId).orElse(null);
            
            if (docBD == null) {
                logger.error("ERROR AGREGAR_DOCUMENTO: No existe documento con ID " + docId);
                return false;
            }
            
            vehiculoDocumento.setDocumento(docBD);
            
            // Validar que el documento aplica al tipo de vehículo
            if (!documentoAplicaAVehiculo(docBD.getTipoVehiculo(), vehiculo.getTipoVehiculo())) {
                logger.error("ERROR AGREGAR_DOCUMENTO: El documento " + 
                           docBD.getCodigo() + " no aplica para vehículos tipo " + vehiculo.getTipoVehiculo());
                return false;
            }
            
            vehiculoDocumento.setVehiculo(vehiculo);
            vehiculoDocumento.setEstado("En Verificacion");
            vdRepository.save(vehiculoDocumento);
            
            logger.info("Documento agregado exitosamente al vehículo ID: " + idVehiculo);
            return true;
            
        } catch (Exception e) {
            logger.error("ERROR AGREGAR_DOCUMENTO: " + e.getMessage());
            return false;
        }
    }
}