package com.example.login.model;

import java.time.LocalDateTime;

public class Bitacora {

    private int idBitacora;
    private int idUsuario;
    private Integer idUsuarioAfectado; // puede ser null
    private String accion;
    private String descripcion;
    private LocalDateTime fecha;

    public Bitacora() {
    }

    public Bitacora(int idUsuario, String accion, String descripcion) {
        this.idUsuario = idUsuario;
        this.accion = accion;
        this.descripcion = descripcion;
    }

    public int getIdBitacora() {
        return idBitacora;
    }

    public void setIdBitacora(int idBitacora) {
        this.idBitacora = idBitacora;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdUsuarioAfectado() {
        return idUsuarioAfectado;
    }

    public void setIdUsuarioAfectado(Integer idUsuarioAfectado) {
        this.idUsuarioAfectado = idUsuarioAfectado;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
