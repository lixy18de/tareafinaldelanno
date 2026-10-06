package com.example.login.controller;

import com.example.login.model.Bitacora;
import com.example.login.model.LoginModel;
import com.example.login.model.Usuario;
import com.example.login.repository.BitacoraRepository;
import com.example.login.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class LoginWebController {

    private final UsuarioRepository usuarioRepository;
    private final BitacoraRepository bitacoraRepository;

    public LoginWebController(UsuarioRepository usuarioRepository, BitacoraRepository bitacoraRepository) {
        this.usuarioRepository = usuarioRepository;
        this.bitacoraRepository = bitacoraRepository;
    }

    @GetMapping("/")
    public String raiz() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute LoginModel loginModel, Model model, HttpSession session) {
        Optional<Usuario> usuarioOpt = usuarioRepository.autenticar(loginModel.getUsername(), loginModel.getPassword());

        if (usuarioOpt.isEmpty()) {
            model.addAttribute("error", "Usuario o contraseña incorrectos, o el usuario está deshabilitado.");
            return "login";
        }

        Usuario usuario = usuarioOpt.get();
        session.setAttribute("usuario", usuario.getUsername());
        session.setAttribute("idUsuario", usuario.getIdUsuario());

        bitacoraRepository.registrar(new Bitacora(usuario.getIdUsuario(), "LOGIN", "Inicio de sesión de " + usuario.getUsername()));

        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        Object idUsuario = session.getAttribute("idUsuario");
        Object username = session.getAttribute("usuario");
        if (idUsuario != null) {
            bitacoraRepository.registrar(new Bitacora((Integer) idUsuario, "LOGOUT", "Cierre de sesión de " + username));
        }
        session.invalidate();
        return "redirect:/login";
    }
}
