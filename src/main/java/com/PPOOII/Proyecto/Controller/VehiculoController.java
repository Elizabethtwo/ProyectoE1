package com.PPOOII.Proyecto.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.PPOOII.Proyecto.Entities.Vehiculo;
import com.PPOOII.Proyecto.Entities.VehiculoDocumento;
import com.PPOOII.Proyecto.Services.VehiculoServiceImpl;

@RestController
@RequestMapping("/v1")
public class VehiculoController {

    // Inyección del servicio 
    @Autowired
    @Qualifier("VehiculoService")
    private VehiculoServiceImpl vehiculoService;

    // POST: Crear vehículo (debe incluir al menos un documento)
    @PostMapping("/vehiculo")
    public boolean agregarVehiculo(@RequestBody Vehiculo vehiculo) {
        return vehiculoService.guardar(vehiculo);
    }

    // PUT: Actualizar vehículo 
    @PutMapping("/vehiculo")
    public boolean editarVehiculo(@RequestBody Vehiculo vehiculo) {
        return vehiculoService.actualizar(vehiculo);
    }

    // DELETE: Eliminar vehículo 
    @DeleteMapping("/vehiculo/{id}")
    public boolean eliminarVehiculo(@PathVariable("id") long id) {
        return vehiculoService.eliminar(id);
    }

    // GET: Listar todos los vehículos (paginado) 
    @GetMapping("/vehiculos")
    public List<Vehiculo> listarVehiculos(Pageable pageable) {
        return vehiculoService.consultarVehiculos(pageable);
    }

    // GET: Buscar vehículo por ID 
    @GetMapping("/vehiculo/id/{id}")
    public Vehiculo getById(@PathVariable("id") long id) {
        return vehiculoService.findById(id);
    }

    // GET: Buscar vehículo por placa 
    @GetMapping("/vehiculo/placa/{placa}")
    public Vehiculo getByPlaca(@PathVariable("placa") String placa) {
        return vehiculoService.findByPlaca(placa);
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
                                    @RequestBody VehiculoDocumento vehiculoDocumento) {
        return vehiculoService.agregarDocumento(idVehiculo, vehiculoDocumento);
    }
}
