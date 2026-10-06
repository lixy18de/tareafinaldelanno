package com.example.login.repository;

import com.example.login.model.HistorialAcademico;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HistorialAcademicoRepository {

    private final DatabaseConnection databaseConnection;

    public HistorialAcademicoRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void save(HistorialAcademico h) {
        String sql = "INSERT INTO historial_academico (id_estudiante, \"año\", sede, nivel, grado, seccion, fecha_ingreso, fecha_fin) " +
                "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, h.getIdEstudiante());
            ps.setInt(2, h.getAnio());
            ps.setString(3, h.getSede());
            ps.setString(4, h.getNivel());
            ps.setString(5, h.getGrado());
            ps.setString(6, h.getSeccion());
            ps.setObject(7, h.getFechaIngreso());
            ps.setObject(8, h.getFechaFin());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando historial academico: " + e.getMessage(), e);
        }
    }

    /**
     * Lista las matriculas (historial_academico) trayendo tambien el nombre
     * del estudiante, tal como se ve en la tabla de "Gestion de Matriculas".
     */
    public List<HistorialAcademico> findAllConEstudiante() {
        List<HistorialAcademico> lista = new ArrayList<>();
        String sql = "SELECT h.*, e.primer_nombre, e.segundo_nombre, e.primer_apellido, e.segundo_apellido " +
                "FROM historial_academico h " +
                "JOIN estudiante e ON e.id_estudiante = h.id_estudiante " +
                "ORDER BY h.id_historial DESC";
        try (Connection conn = databaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                HistorialAcademico h = mapRow(rs);
                String nombre = rs.getString("primer_nombre");
                String segundoNombre = rs.getString("segundo_nombre");
                String apellido = rs.getString("primer_apellido");
                String segundoApellido = rs.getString("segundo_apellido");
                StringBuilder sb = new StringBuilder(nombre != null ? nombre : "");
                if (segundoNombre != null && !segundoNombre.isBlank()) sb.append(" ").append(segundoNombre);
                sb.append(" ").append(apellido != null ? apellido : "");
                if (segundoApellido != null && !segundoApellido.isBlank()) sb.append(" ").append(segundoApellido);
                h.setNombreEstudiante(sb.toString().trim());
                lista.add(h);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando matriculas: " + e.getMessage(), e);
        }
        return lista;
    }

    public void eliminar(int idHistorial) {
        String sql = "DELETE FROM historial_academico WHERE id_historial = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHistorial);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando matricula: " + e.getMessage(), e);
        }
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM historial_academico WHERE fecha_fin IS NULL";
        try (Connection conn = databaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error contando matriculas activas: " + e.getMessage(), e);
        }
        return 0;
    }

    private HistorialAcademico mapRow(ResultSet rs) throws SQLException {
        HistorialAcademico h = new HistorialAcademico();
        h.setIdHistorial(rs.getInt("id_historial"));
        h.setIdEstudiante(rs.getInt("id_estudiante"));
        h.setAnio(rs.getInt("año"));
        h.setSede(rs.getString("sede"));
        h.setNivel(rs.getString("nivel"));
        h.setGrado(rs.getString("grado"));
        h.setSeccion(rs.getString("seccion"));
        Date fi = rs.getDate("fecha_ingreso");
        h.setFechaIngreso(fi != null ? fi.toLocalDate() : null);
        Date ff = rs.getDate("fecha_fin");
        h.setFechaFin(ff != null ? ff.toLocalDate() : null);
        return h;
    }
}
