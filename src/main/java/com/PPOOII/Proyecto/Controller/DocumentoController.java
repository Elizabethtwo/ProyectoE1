package com.PPOOII.Proyecto.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.PPOOII.Proyecto.Entities.Documento;
import com.PPOOII.Proyecto.Services.Interfaces.IDocumentoService;

@RestController
@RequestMapping("/v1")
public class DocumentoController {

    // Inyección del servicio (depende de la interfaz, no de la implementación)
    @Autowired
    @Qualifier("DocumentoService")
    private IDocumentoService documentoService;

    // POST 
    @PostMapping("/documento")
    public boolean agregarDocumento(@Valid @RequestBody Documento documento) {
        return documentoService.guardar(documento);
    }

    // PUT 
    @PutMapping("/documento")
    public boolean editarDocumento(@Valid @RequestBody Documento documento) {
        return documentoService.actualizar(documento);
    }

    // DELETE
    @DeleteMapping("/documento/{id}")
    public boolean eliminarDocumento(@PathVariable("id") long id) {
        return documentoService.eliminar(id);}
    @GetMapping("/documentos")
    public List<Documento> listarDocumentos(@NonNull Pageable pageable) {
        return documentoService.consultarDocumentos(pageable);
    }

    // GET: Por ID 
    @GetMapping("/documento/id/{id}")
    public Documento getById(@PathVariable("id") long id) {
        return documentoService.findById(id);
    }

    // GET: Por código 
    @GetMapping("/documento/codigo/{codigo}")
    public Documento getByCodigo(@PathVariable("codigo") String codigo) {
        return documentoService.findByCodigo(codigo);
    }

    // GET: Por tipo de vehículo al que aplica 
    @GetMapping("/documento/tipoVehiculo/{tipoVehiculo}")
    public List<Documento> getByTipoVehiculo(@PathVariable("tipoVehiculo") String tipoVehiculo) {
        return documentoService.findByTipoVehiculo(tipoVehiculo);
    }
}
