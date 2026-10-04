package com.PPOOII.Proyecto.Services.Interfaces;

import java.util.List;

public interface IVehiculoConductorService {

    boolean cambiarEstado(long idVehiculo, long idPersona, String nuevoEstado);
    boolean asociarVehiculos(long idPersona, List<Long> idsVehiculos);
}
