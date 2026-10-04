package com.PPOOII.Proyecto.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PPOOII.Proyecto.Entities.Documento;
import com.PPOOII.Proyecto.Entities.Vehiculo;
import com.PPOOII.Proyecto.Entities.VehiculoDocumento;
import com.PPOOII.Proyecto.Repository.DocumentoRepository;
import com.PPOOII.Proyecto.Repository.VehiculoRepository;

@RestController
@RequestMapping("/v1")
public class VehiculoDocumentoCargaController {

    @Autowired
    @Qualifier("IDocumentoRepo")
    private DocumentoRepository documentoRepository;

    @Autowired
    @Qualifier("IVehiculoRepo")
    private VehiculoRepository vehiculoRepository;

    @PostMapping("/vehiculo/documentos")
    public ResponseEntity<?> cargarDocumentosVehiculo(@RequestBody List<Map<String, Object>> payload) {
        if (payload == null || payload.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Debe enviar al menos un documento"));
        }

        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Map<String, Object> item : payload) {
            Long idVehiculo = item.containsKey("idVehiculo") ? ((Number) item.get("idVehiculo")).longValue() : null;
            Long idDocumento = item.containsKey("idDocumento") ? ((Number) item.get("idDocumento")).longValue() : null;
            String archivoBase64 = item.get("archivoBase64") != null ? item.get("archivoBase64").toString() : null;
            String nombreArchivo = item.get("nombreArchivo") != null ? item.get("nombreArchivo").toString() : null;
            String fechaExpedicion = item.get("fechaExpedicion") != null ? item.get("fechaExpedicion").toString() : null;
            String fechaVencimiento = item.get("fechaVencimiento") != null ? item.get("fechaVencimiento").toString() : null;

            if (idVehiculo == null || idDocumento == null || archivoBase64 == null || archivoBase64.isBlank()) {
                resultado.add(Map.of("ok", false, "error", "Faltan datos obligatorios para cargar documento"));
                continue;
            }

            Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo).orElse(null);
            Documento documento = documentoRepository.findById(idDocumento).orElse(null);
            if (vehiculo == null || documento == null) {
                resultado.add(Map.of("ok", false, "idVehiculo", idVehiculo, "idDocumento", idDocumento, "error", "Vehículo o documento no existen"));
                continue;
            }

            VehiculoDocumento relacion = new VehiculoDocumento();
            relacion.setVehiculo(vehiculo);
            relacion.setDocumento(documento);
            relacion.setFechaExpedicion(LocalDate.parse(fechaExpedicion != null ? fechaExpedicion : LocalDate.now().toString()));
            relacion.setFechaVencimiento(LocalDate.parse(fechaVencimiento != null ? fechaVencimiento : LocalDate.now().plusYears(1).toString()));
            relacion.setNombreArchivo(nombreArchivo);
            relacion.setArchivoBase64(archivoBase64);
            relacion.setEstado("En Verificacion");

            vehiculo.getDocumentos().add(relacion);
            vehiculoRepository.save(vehiculo);

            resultado.add(Map.of(
                    "ok", true,
                    "idVehiculo", idVehiculo,
                    "idDocumento", idDocumento,
                    "nombreArchivo", nombreArchivo,
                    "estado", "En Verificacion"
            ));
        }

        return ResponseEntity.ok(resultado);
    }
}
