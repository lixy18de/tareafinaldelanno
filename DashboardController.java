package com.example.login.controller;

import com.example.login.repository.EstudianteRepository;
import com.example.login.repository.HistorialAcademicoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final EstudianteRepository estudianteRepository;
    private final HistorialAcademicoRepository historialAcademicoRepository;

    public DashboardController(EstudianteRepository estudianteRepository,
                                HistorialAcademicoRepository historialAcademicoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.historialAcademicoRepository = historialAcademicoRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalEstudiantes", estudianteRepository.count());
        model.addAttribute("totalMatriculasActivas", historialAcademicoRepository.count());
        // Pagos y reportes no tienen tabla propia todavia; se dejan en 0 por ahora.
        model.addAttribute("pagosPendientes", 0);
        model.addAttribute("reportesGenerados", 0);
        return "dashboard";
    }
}
