package com.example.login.controller;

import com.example.login.model.Bitacora;
import com.example.login.model.Usuario;
import com.example.login.repository.BitacoraRepository;
import com.example.login.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/configuracion")
public class ConfiguracionController {

    private final UsuarioRepository usuarioRepository;
    private final BitacoraRepository bitacoraRepository;

    public ConfiguracionController(UsuarioRepository usuarioRepository, BitacoraRepository bitacoraRepository) {
        this.usuarioRepository = usuarioRepository;
        this.bitacoraRepository = bitacoraRepository;
    }

    @GetMapping
    public String mostrarConfiguracion(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("nuevoUsuario", new Usuario());
        return "configuracion";
    }

    @PostMapping("/agregar")
    public String agregarUsuario(@ModelAttribute Usuario nuevoUsuario, Model model, HttpSession session) {
        try {
            usuarioRepository.save(nuevoUsuario);
            Integer idUsuario = (Integer) session.getAttribute("idUsuario");
            if (idUsuario != null) {
                bitacoraRepository.registrar(new Bitacora(idUsuario, "CREAR_USUARIO",
                        "Se creó el usuario " + nuevoUsuario.getUsername()));
            }
        } catch (Exception ex) {
            model.addAttribute("error", "No se pudo crear el usuario: " + ex.getMessage());
        }
        return "redirect:/configuracion";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable("id") int id,
                                 @RequestParam("nuevoEstado") String nuevoEstado,
                                 HttpSession session) {
        usuarioRepository.actualizarEstado(id, nuevoEstado);
        Integer idUsuario = (Integer) session.getAttribute("idUsuario");
        if (idUsuario != null) {
            bitacoraRepository.registrar(new Bitacora(idUsuario, "CAMBIO_ESTADO_USUARIO",
                    "Usuario id " + id + " cambiado a estado " + nuevoEstado));
        }
        return "redirect:/configuracion";
    }
}
