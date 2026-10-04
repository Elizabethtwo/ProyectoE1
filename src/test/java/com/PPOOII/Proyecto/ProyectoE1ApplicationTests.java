package com.PPOOII.Proyecto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.PPOOII.Proyecto.Repository.TrayectoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class ProyectoE1ApplicationTests {

	@Autowired
	private TrayectoRepository trayectoRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void trayectoSchemaAndQueriesAreAvailable() {
		assertEquals(1, jdbcTemplate.queryForObject("PRAGMA foreign_keys", Integer.class));
		assertNotNull(trayectoRepository.findRoute("ruta-inexistente"));
		assertNotNull(trayectoRepository.findRouteCodesByDriver("identificacion-inexistente"));
		assertNotNull(trayectoRepository.findRoutesAndDriversByPlate("placa-inexistente"));
		assertNotNull(trayectoRepository.findRoutesWithoutAuthorization());
		assertNotNull(trayectoRepository.findByLatitudIsNullOrLongitudIsNull());
	}

}
