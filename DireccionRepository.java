package com.example.login.repository;

import com.example.login.model.Direccion;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class DireccionRepository {

    private final DatabaseConnection databaseConnection;

    public DireccionRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Guarda una direccion y devuelve el id generado, para usarlo como
     * id_direccion al guardar el estudiante. Si todos los campos vienen vacios,
     * devuelve null (no crea una fila vacia).
     */
    public Integer save(Direccion d) {
        boolean vacia = isBlank(d.getZona()) && isBlank(d.getDepartamento()) && isBlank(d.getMunicipio())
                && isBlank(d.getCanton()) && isBlank(d.getCaserio()) && isBlank(d.getTipoCalle())
                && isBlank(d.getDireccionDetalle());
        if (vacia) {
            return null;
        }

        String sql = "INSERT INTO direccion (zona, departamento, municipio, canton, caserio, tipo_calle, direccion_detalle) " +
                "VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, d.getZona());
            ps.setString(2, d.getDepartamento());
            ps.setString(3, d.getMunicipio());
            ps.setString(4, d.getCanton());
            ps.setString(5, d.getCaserio());
            ps.setString(6, d.getTipoCalle());
            ps.setString(7, d.getDireccionDetalle());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando direccion: " + e.getMessage(), e);
        }
        return null;
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
