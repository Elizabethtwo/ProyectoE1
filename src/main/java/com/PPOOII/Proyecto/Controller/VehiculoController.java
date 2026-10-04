package com.PPOOII.Proyecto.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import com.PPOOII.Proyecto.Entities.Vehiculo;
import com.PPOOII.Proyecto.Entities.VehiculoDocumento;
import com.PPOOII.Proyecto.Repository.VehiculoConductorRepository;
import com.PPOOII.Proyecto.Services.Interfaces.IVehiculoService;

@RestController
@RequestMapping("/v1")
public class VehiculoController {

    // Inyección del servicio (depende de la interfaz, no de la implementación)
    @Autowired
    @Qualifier("VehiculoService")
    private IVehiculoService vehiculoService;

    @Autowired
    @Qualifier("IVehiculoConductorRepo")
    private VehiculoConductorRepository vehiculoConductorRepository;

    // POST: Crear vehículo (debe incluir al menos un documento)
    @PostMapping("/vehiculo")
    public boolean agregarVehiculo(@Valid @RequestBody Vehiculo vehiculo) {
        return vehiculoService.guardar(vehiculo);
    }

    // PUT: Actualizar vehículo 
    @PutMapping("/vehiculo")
    public boolean editarVehiculo(@Valid @RequestBody Vehiculo vehiculo) {
        return vehiculoService.actualizar(vehiculo);
    }

    // DELETE: Eliminar vehículo 
    @DeleteMapping("/vehiculo/{id}")
    public boolean eliminarVehiculo(@PathVariable("id") long id) {
        return vehiculoService.eliminar(id);
    }

    // GET: Listar todos los vehículos (paginado) 
    @GetMapping("/vehiculos")
    public List<Vehiculo> listarVehiculos(@NonNull Pageable pageable) {
        return vehiculoService.consultarVehiculos(pageable);
    }

    // GET: Buscar vehículo por ID 
    @GetMapping("/vehiculo/id/{id}")
    public Vehiculo getById(@PathVariable("id") long id) {
        return vehiculoService.findById(id);
    }

    // GET: Buscar vehículo por placa 
    @GetMapping("/vehiculo/placa/{placa}")
    public ResponseEntity<?> getByPlaca(@PathVariable("placa") String placa) {
        Vehiculo vehiculo = vehiculoService.findByPlaca(placa);
        if (vehiculo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(Map.of(
                "vehiculo", vehiculo,
                "conductores", vehiculoConductorRepository.findByVehiclePlate(vehiculo.getPlaca())
        ));
    }

    // GET: Buscar vehículos por tipo (Automovil / Motocicleta) 
    @GetMapping("/vehiculo/tipo/{tipoVehiculo}")
    public List<Vehiculo> getByTipoVehiculo(@PathVariable("tipoVehiculo") String tipoVehiculo) {
        return vehiculoService.findByTipoVehiculo(tipoVehiculo);
    }

    // GET: Buscar vehículos que tengan un tipo de documento en común
    @GetMapping("/vehiculo/documento/{codigoDocumento}")
    public List<Vehiculo> getByCodigoDocumento(@PathVariable("codigoDocumento") String codigoDocumento) {
        return vehiculoService.findByCodigoDocumento(codigoDocumento);
    }

    // GET: Buscar vehículos por estado de documento 
    @GetMapping("/vehiculo/estado/{estado}")
    public List<Vehiculo> getByEstadoDocumento(@PathVariable("estado") String estado) {
        return vehiculoService.findByEstadoDocumento(estado);
    }

    // POST: Agregar documento a un vehículo existente 
    @PostMapping("/vehiculo/{idVehiculo}/documento")
    public boolean agregarDocumento(@PathVariable("idVehiculo") long idVehiculo,
                                    @Valid @RequestBody VehiculoDocumento vehiculoDocumento) {
        return vehiculoService.agregarDocumento(idVehiculo, vehiculoDocumento);
    }
}
