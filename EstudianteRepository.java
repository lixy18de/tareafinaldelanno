package com.example.login.repository;

import com.example.login.model.Estudiante;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EstudianteRepository {

    private final DatabaseConnection databaseConnection;

    public EstudianteRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public int save(Estudiante e) {
        String sql = "INSERT INTO estudiante (nie, nui, dui, primer_nombre, segundo_nombre, tercer_nombre, " +
                "primer_apellido, segundo_apellido, tercer_apellido, nombre_partida, fecha_nacimiento, " +
                "nacionalidad, departamento_nacimiento, municipio_nacimiento, sexo, estado_familiar, etnia, " +
                "discapacidad, trastorno_aprendizaje, embarazada, fecha_probable_parto, estado_persona, " +
                "correo, id_direccion) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNie());
            ps.setString(2, e.getNui());
            ps.setString(3, e.getDui());
            ps.setString(4, e.getPrimerNombre());
            ps.setString(5, e.getSegundoNombre());
            ps.setString(6, e.getTercerNombre());
            ps.setString(7, e.getPrimerApellido());
            ps.setString(8, e.getSegundoApellido());
            ps.setString(9, e.getTercerApellido());
            ps.setString(10, e.getNombrePartida());
            ps.setObject(11, e.getFechaNacimiento());
            ps.setString(12, e.getNacionalidad());
            ps.setString(13, e.getDepartamentoNacimiento());
            ps.setString(14, e.getMunicipioNacimiento());
            ps.setString(15, e.getSexo());
            ps.setString(16, e.getEstadoFamiliar());
            ps.setString(17, e.getEtnia());
            ps.setBoolean(18, e.isDiscapacidad());
            ps.setBoolean(19, e.isTrastornoAprendizaje());
            ps.setBoolean(20, e.isEmbarazada());
            ps.setObject(21, e.getFechaProbableParto());
            ps.setString(22, e.getEstadoPersona() != null ? e.getEstadoPersona() : "vive");
            ps.setString(23, e.getCorreo());
            if (e.getIdDireccion() != null) {
                ps.setInt(24, e.getIdDireccion());
            } else {
                ps.setNull(24, Types.INTEGER);
            }
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error guardando estudiante: " + ex.getMessage(), ex);
        }
        return -1;
    }

    public List<Estudiante> findAll() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiante ORDER BY id_estudiante DESC";
        try (Connection conn = databaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando estudiantes: " + e.getMessage(), e);
        }
        return lista;
    }

    public Optional<Estudiante> findById(int id) {
        String sql = "SELECT * FROM estudiante WHERE id_estudiante = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando estudiante: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM estudiante";
        try (Connection conn = databaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error contando estudiantes: " + e.getMessage(), e);
        }
        return 0;
    }

    private Estudiante mapRow(ResultSet rs) throws SQLException {
        Estudiante e = new Estudiante();
        e.setIdEstudiante(rs.getInt("id_estudiante"));
        e.setNie(rs.getString("nie"));
        e.setNui(rs.getString("nui"));
        e.setDui(rs.getString("dui"));
        e.setPrimerNombre(rs.getString("primer_nombre"));
        e.setSegundoNombre(rs.getString("segundo_nombre"));
        e.setTercerNombre(rs.getString("tercer_nombre"));
        e.setPrimerApellido(rs.getString("primer_apellido"));
        e.setSegundoApellido(rs.getString("segundo_apellido"));
        e.setTercerApellido(rs.getString("tercer_apellido"));
        e.setNombrePartida(rs.getString("nombre_partida"));
        Date fn = rs.getDate("fecha_nacimiento");
        e.setFechaNacimiento(fn != null ? fn.toLocalDate() : null);
        e.setNacionalidad(rs.getString("nacionalidad"));
        e.setDepartamentoNacimiento(rs.getString("departamento_nacimiento"));
        e.setMunicipioNacimiento(rs.getString("municipio_nacimiento"));
        e.setSexo(rs.getString("sexo"));
        e.setEstadoFamiliar(rs.getString("estado_familiar"));
        e.setEtnia(rs.getString("etnia"));
        e.setDiscapacidad(rs.getBoolean("discapacidad"));
        e.setTrastornoAprendizaje(rs.getBoolean("trastorno_aprendizaje"));
        e.setEmbarazada(rs.getBoolean("embarazada"));
        Date fpp = rs.getDate("fecha_probable_parto");
        e.setFechaProbableParto(fpp != null ? fpp.toLocalDate() : null);
        e.setEstadoPersona(rs.getString("estado_persona"));
        e.setCorreo(rs.getString("correo"));
        int idDir = rs.getInt("id_direccion");
        e.setIdDireccion(rs.wasNull() ? null : idDir);
        return e;
    }
}
