package com.example.login.model;

import java.time.LocalDateTime;

public class Usuario {

    private int idUsuario;
    private String username;
    private String password; // en la BD siempre queda como hash bcrypt (trigger lo encripta solo)
    private LocalDateTime ingreso;
    private String estado; // "activo" / "Deshabilitado"

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getIngreso() {
        return ingreso;
    }

    public void setIngreso(LocalDateTime ingreso) {
        this.ingreso = ingreso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
