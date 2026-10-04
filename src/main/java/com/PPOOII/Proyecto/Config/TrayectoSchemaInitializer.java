package com.PPOOII.Proyecto.Config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TrayectoSchemaInitializer implements ApplicationRunner {

    private final DataSource dataSource;

    public TrayectoSchemaInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            if (hasRequiredForeignKeys(connection)) {
                return;
            }
            migrateTrayectoTable(connection);
        }
    }

    private boolean hasRequiredForeignKeys(Connection connection) throws SQLException {
        boolean personaReference = false;
        boolean vehicleReference = false;
        try (Statement statement = connection.createStatement();
             ResultSet keys = statement.executeQuery("PRAGMA foreign_key_list('trayecto')")) {
            while (keys.next()) {
                String table = keys.getString("table");
                personaReference |= "persona".equalsIgnoreCase(table);
                vehicleReference |= "vehiculo".equalsIgnoreCase(table);
            }
        }
        return personaReference && vehicleReference;
    }

    private void migrateTrayectoTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = OFF");
            connection.setAutoCommit(false);
            statement.execute("ALTER TABLE trayecto RENAME TO trayecto_sin_claves_foraneas");
            statement.execute("""
                    CREATE TABLE trayecto (
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
                    )
                    """);
            statement.executeUpdate("""
                    INSERT INTO trayecto (id, idpersona, idvehiculo, codigo_ruta, ubicacion,
                                          orden_parada, latitud, longitud, login_registro)
                    SELECT id, idpersona, idvehiculo, codigo_ruta, ubicacion,
                           orden_parada, latitud, longitud, login_registro
                    FROM trayecto_sin_claves_foraneas
                    """);
            statement.execute("DROP TABLE trayecto_sin_claves_foraneas");
            statement.execute("CREATE UNIQUE INDEX uk_trayecto_ruta_orden "
                    + "ON trayecto(codigo_ruta, orden_parada)");
            connection.commit();
        } catch (SQLException exception) {
            connection.rollback();
            throw exception;
        } finally {
            connection.setAutoCommit(true);
            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON");
            }
        }
    }
}
