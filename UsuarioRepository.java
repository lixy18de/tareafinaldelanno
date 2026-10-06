package com.example.login.repository;

import com.example.login.model.Usuario;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepository {

    private final DatabaseConnection databaseConnection;

    public UsuarioRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Valida usuario y contrasena usando la funcion crypt() de pgcrypto,
     * que es la misma extension que usa el trigger para encriptar al guardar.
     * Solo devuelve el usuario si la contrasena es correcta Y el estado es 'activo'.
     */
    public Optional<Usuario> autenticar(String username, String plainPassword) {
        String sql = "SELECT * FROM usuario WHERE username = ? AND password = crypt(?, password) AND estado = 'activo'";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, plainPassword);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error autenticando usuario: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public Optional<Usuario> findByUsername(String username) {
        String sql = "SELECT * FROM usuario WHERE username = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando usuario: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY id_usuario DESC";
        try (Connection conn = databaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando usuarios: " + e.getMessage(), e);
        }
        return usuarios;
    }

    /**
     * Inserta un usuario nuevo. La contrasena va en texto plano aqui;
     * el trigger trigger_bcrypt_password de la base la encripta automaticamente
     * al hacer el INSERT, asi que NO se debe encriptar de este lado.
     */
    public void save(Usuario usuario) {
        String sql = "INSERT INTO usuario (username, password, ingreso, estado) VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getEstado() != null ? usuario.getEstado() : "activo");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando usuario: " + e.getMessage(), e);
        }
    }

    public void actualizarEstado(int idUsuario, String nuevoEstado) {
        String sql = "UPDATE usuario SET estado = ? WHERE id_usuario = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando estado de usuario: " + e.getMessage(), e);
        }
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        Timestamp ts = rs.getTimestamp("ingreso");
        u.setIngreso(ts != null ? ts.toLocalDateTime() : null);
        u.setEstado(rs.getString("estado"));
        return u;
    }
}
