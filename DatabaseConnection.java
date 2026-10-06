package com.example.login.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Maneja la conexion JDBC pura hacia MySQL.
 * Los repositorios piden una Connection nueva cada vez que la necesitan
 * y son responsables de cerrarla (usar try-with-resources).
 */
@Component
public class DatabaseConnection {

    @Value("${db.url}")
    private String url;

    @Value("${db.user}")
    private String user;

    @Value("${db.password}")
    private String password;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
