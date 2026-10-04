CREATE TABLE IF NOT EXISTS trayecto (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    idpersona BIGINT NOT NULL,
    idvehiculo BIGINT NOT NULL,
    codigo_ruta VARCHAR(50) NOT NULL,
    ubicacion VARCHAR(255) NOT NULL,
    orden_parada INTEGER NOT NULL CHECK (orden_parada >= 0),
    latitud FLOAT,
    longitud FLOAT,
    login_registro VARCHAR(100) NOT NULL,
    FOREIGN KEY (idpersona) REFERENCES persona(id),
    FOREIGN KEY (idvehiculo) REFERENCES vehiculo(id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_trayecto_ruta_orden
    ON trayecto(codigo_ruta, orden_parada);
