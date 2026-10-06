package com.example.login.repository;

import com.example.login.model.Bitacora;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BitacoraRepository {

    private final DatabaseConnection databaseConnection;

    public BitacoraRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void registrar(Bitacora b) {
        String sql = "INSERT INTO bitacora (id_usuario, id_usuarioafectado, accion, descripcion, fecha) " +
                "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, b.getIdUsuario());
            if (b.getIdUsuarioAfectado() != null) {
                ps.setInt(2, b.getIdUsuarioAfectado());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, b.getAccion());
            ps.setString(4, b.getDescripcion());
            ps.executeUpdate();
        } catch (SQLException e) {
            // No queremos que un fallo al escribir bitacora tumbe la operacion principal;
            // solo lo dejamos en consola.
            System.err.println("No se pudo registrar en bitacora: " + e.getMessage());
        }
    }

    public List<Bitacora> findAll() {
        List<Bitacora> lista = new ArrayList<>();
        String sql = "SELECT * FROM bitacora ORDER BY fecha DESC";
        try (Connection conn = databaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando bitacora: " + e.getMessage(), e);
        }
        return lista;
    }

    private Bitacora mapRow(ResultSet rs) throws SQLException {
        Bitacora b = new Bitacora();
        b.setIdBitacora(rs.getInt("id_bitacora"));
        b.setIdUsuario(rs.getInt("id_usuario"));
        int afectado = rs.getInt("id_usuarioafectado");
        b.setIdUsuarioAfectado(rs.wasNull() ? null : afectado);
        b.setAccion(rs.getString("accion"));
        b.setDescripcion(rs.getString("descripcion"));
        Timestamp ts = rs.getTimestamp("fecha");
        b.setFecha(ts != null ? ts.toLocalDateTime() : null);
        return b;
    }
}
