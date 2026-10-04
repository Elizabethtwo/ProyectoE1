package com.PPOOII.Proyecto.Controller;

import java.time.LocalDate;
import java.util.Base64;
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
import com.PPOOII.Proyecto.Repository.VehiculoDocumentoRepository;

@RestController
@RequestMapping("/v1")
public class VehiculoDocumentoCargaController {

    @Autowired
    @Qualifier("IDocumentoRepo")
    private DocumentoRepository documentoRepository;

    @Autowired
    @Qualifier("IVehiculoRepo")
    private VehiculoRepository vehiculoRepository;

    @Autowired
    @Qualifier("IVehiculoDocumentoRepo")
    private VehiculoDocumentoRepository vehiculoDocumentoRepository;

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

            byte[] archivoPdf;
            try {
                archivoPdf = Base64.getDecoder().decode(archivoBase64);
            } catch (IllegalArgumentException e) {
                resultado.add(Map.of("ok", false, "error", "El documento no contiene un Base64 válido"));
                continue;
            }
            if (archivoPdf.length < 5 || archivoPdf[0] != '%' || archivoPdf[1] != 'P'
                    || archivoPdf[2] != 'D' || archivoPdf[3] != 'F' || archivoPdf[4] != '-') {
                resultado.add(Map.of("ok", false, "error", "El archivo debe ser un PDF"));
                continue;
            }

            Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo).orElse(null);
            Documento documento = documentoRepository.findById(idDocumento).orElse(null);
            if (vehiculo == null || documento == null) {
                resultado.add(Map.of("ok", false, "idVehiculo", idVehiculo, "idDocumento", idDocumento, "error", "Vehículo o documento no existen"));
                continue;
            }

            VehiculoDocumento relacion = vehiculoDocumentoRepository
                    .findByVehiculoIdAndDocumentoId(idVehiculo, idDocumento)
                    .orElseGet(VehiculoDocumento::new);
            relacion.setVehiculo(vehiculo);
            relacion.setDocumento(documento);
            try {
                relacion.setFechaExpedicion(LocalDate.parse(
                        fechaExpedicion != null ? fechaExpedicion : LocalDate.now().toString()));
                relacion.setFechaVencimiento(LocalDate.parse(
                        fechaVencimiento != null ? fechaVencimiento : LocalDate.now().plusYears(1).toString()));
            } catch (java.time.format.DateTimeParseException e) {
                resultado.add(Map.of("ok", false, "error", "Las fechas deben tener formato AAAA-MM-DD"));
                continue;
            }
            relacion.setNombreArchivo(nombreArchivo);
            relacion.setArchivoBase64(archivoPdf);
            relacion.setEstado("En Verificacion");

            vehiculoDocumentoRepository.save(relacion);

            resultado.add(Map.of(
                    "ok", true,
                    "idVehiculo", idVehiculo,
                    "idDocumento", idDocumento,
                    "nombreArchivo", nombreArchivo == null ? "" : nombreArchivo,
                    "estado", "En Verificacion"
            ));
        }

        return ResponseEntity.ok(resultado);
    }
}
