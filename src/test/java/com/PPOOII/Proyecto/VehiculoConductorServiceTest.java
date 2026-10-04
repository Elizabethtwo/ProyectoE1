package com.PPOOII.Proyecto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.PPOOII.Proyecto.Entities.VehiculoConductor;
import com.PPOOII.Proyecto.Repository.VehiculoConductorRepository;
import com.PPOOII.Proyecto.Services.VehiculoConductorServiceImpl;

@ExtendWith(MockitoExtension.class)
class VehiculoConductorServiceTest {

    @Mock
    private VehiculoConductorRepository vehiculoConductorRepository;

    @InjectMocks
    private VehiculoConductorServiceImpl vehiculoConductorService;

    @Test
    void cambiarEstado_conEstadoValido_actualizaRelacion() {
        VehiculoConductor relacion = new VehiculoConductor();
        relacion.setEstadoConductor("EA");

        when(vehiculoConductorRepository.findByVehiculoIdAndPersonaId(10L, 20L))
            .thenReturn(Optional.of(relacion));

        boolean ok = vehiculoConductorService.cambiarEstado(10L, 20L, "PO");

        assertTrue(ok);
        assertEquals("PO", relacion.getEstadoConductor());
        verify(vehiculoConductorRepository).save(relacion);
    }

    @Test
    void cambiarEstado_conEstadoInvalido_retornaFalse() {
        boolean ok = vehiculoConductorService.cambiarEstado(10L, 20L, "XX");

        assertFalse(ok);
    }
}
