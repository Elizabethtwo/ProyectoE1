package com.PPOOII.Proyecto.Services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.PPOOII.Proyecto.Entities.Documento;
import com.PPOOII.Proyecto.Repository.DocumentoRepository;
import com.PPOOII.Proyecto.Services.Interfaces.IDocumentoService;

@Service("DocumentoService")
public class DocumentoServiceImpl implements IDocumentoService {

    @Autowired
    @Qualifier("IDocumentoRepo")
    private DocumentoRepository documentoRepository;

    private static final Logger logger = LogManager.getLogger(DocumentoServiceImpl.class);

    // INSERT 
    @Override
    public boolean guardar(Documento documento) {
        try {
            if (documento == null) {
                logger.error("ERROR GUARDAR_DOCUMENTO: El documento es nulo.");
                return false;
            }
            documentoRepository.save(documento);
            return true;
        } catch (Exception e) {
            logger.error("ERROR GUARDAR_DOCUMENTO: " + e.getMessage());
            return false;
        }
    }

    // UPDATE 
    @Override
    public boolean actualizar(Documento documento) {
        try {
            if (documento == null || documento.getId() == null || documento.getId() == 0) {
                logger.error("ERROR ACTUALIZAR_DOCUMENTO: Documento nulo o ID 0.");
                return false;
            }
            documentoRepository.save(documento);
            return true;
        } catch (Exception e) {
            logger.error("ERROR ACTUALIZAR_DOCUMENTO: " + e.getMessage());
            return false;
        }
    }

    // DELETE 
    @Override
    public boolean eliminar(long id) {
        try {
            if (id == 0) {
                logger.error("ERROR ELIMINAR_DOCUMENTO: ID es 0.");
                return false;
            }
            Documento doc = documentoRepository.findById(id).orElse(null);
            if (doc == null) {
                logger.error("ERROR ELIMINAR_DOCUMENTO: No existe documento con ID " + id);
                return false;
            }
            documentoRepository.delete(doc);
            return true;
        } catch (Exception e) {
            logger.error("ERROR ELIMINAR_DOCUMENTO: " + e.getMessage());
            return false;
        }
    }

    // LIST 
    @Override
    public List<Documento> consultarDocumentos(Pageable pageable) {
        return documentoRepository.findAll(pageable).getContent();
    }

    // FIND BY
    @Override
    public Documento findById(long id) {
        return documentoRepository.findById(id).orElse(null);
    }

    @Override
    public Documento findByCodigo(String codigo) {
        return documentoRepository.findByCodigo(codigo).orElse(null);
    }

    @Override
    public List<Documento> findByTipoVehiculo(String tipoVehiculo) {
        return documentoRepository.findByTipoVehiculo(tipoVehiculo);
    }
}
