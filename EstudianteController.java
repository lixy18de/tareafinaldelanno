package com.example.login.controller;

import com.example.login.model.Direccion;
import com.example.login.model.Estudiante;
import com.example.login.model.HistorialAcademico;
import com.example.login.repository.DireccionRepository;
import com.example.login.repository.EstudianteRepository;
import com.example.login.repository.HistorialAcademicoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Year;

@Controller
public class EstudianteController {

    private final EstudianteRepository estudianteRepository;
    private final DireccionRepository direccionRepository;
    private final HistorialAcademicoRepository historialAcademicoRepository;

    public EstudianteController(EstudianteRepository estudianteRepository,
                                 DireccionRepository direccionRepository,
                                 HistorialAcademicoRepository historialAcademicoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.direccionRepository = direccionRepository;
        this.historialAcademicoRepository = historialAcademicoRepository;
    }

    @GetMapping("/registrar-estudiante")
    public String mostrarFormulario(Model model) {
        model.addAttribute("estudiante", new Estudiante());
        model.addAttribute("direccion", new Direccion());
        return "registrar-estudiante";
    }

    @PostMapping("/registrar-estudiante")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante,
                                     @ModelAttribute Direccion direccion,
                                     Model model) {
        try {
            Integer idDireccion = direccionRepository.save(direccion);
            estudiante.setIdDireccion(idDireccion);
            estudianteRepository.save(estudiante);
            model.addAttribute("exito", "Estudiante guardado correctamente.");
        } catch (Exception ex) {
            model.addAttribute("error", "No se pudo guardar el estudiante: " + ex.getMessage());
        }
        model.addAttribute("estudiante", new Estudiante());
        model.addAttribute("direccion", new Direccion());
        return "registrar-estudiante";
    }

    @GetMapping("/gestion-matricula")
    public String gestionMatricula(Model model) {
        model.addAttribute("matriculas", historialAcademicoRepository.findAllConEstudiante());
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("nuevaMatricula", new HistorialAcademico());
        model.addAttribute("anioActual", Year.now().getValue());
        return "gestion-matricula";
    }

    @PostMapping("/gestion-matricula")
    public String crearMatricula(@ModelAttribute HistorialAcademico historial, Model model) {
        try {
            historialAcademicoRepository.save(historial);
        } catch (Exception ex) {
            model.addAttribute("error", "No se pudo crear la matrícula: " + ex.getMessage());
        }
        return "redirect:/gestion-matricula";
    }

    @PostMapping("/gestion-matricula/eliminar/{id}")
    public String eliminarMatricula(@PathVariable("id") int id) {
        historialAcademicoRepository.eliminar(id);
        return "redirect:/gestion-matricula";
    }
}
