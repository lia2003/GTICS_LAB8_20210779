package com.example.gtics_lab8_20210779.controller;

import com.example.gtics_lab8_20210779.entity.Estudiante;
import com.example.gtics_lab8_20210779.repository.EstudianteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteRepository estudianteRepository;

    public EstudianteController(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    // 1. Listar estudiantes por facultad, ordenados por GPA de mayor a menor
    @GetMapping
    public List<Estudiante> listarEstudiantesPorFacultad(@RequestParam String facultad) {
        return estudianteRepository.findByFacultadOrderByGpaDesc(facultad);
    }

    // 2. Agregar un nuevo estudiante y actualizar la clasificación
    @PostMapping
    public ResponseEntity<?> agregarEstudiante(@RequestBody Estudiante nuevoEstudiante) {
        // Verifica si ya hay 10 estudiantes en la facultad
        List<Estudiante> estudiantesFacultad = estudianteRepository.findByFacultadOrderByGpaDesc(nuevoEstudiante.getFacultad());
        if (estudiantesFacultad.size() >= 10) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pueden agregar más de 10 estudiantes a la facultad " + nuevoEstudiante.getFacultad());
        }

        // Guardar el nuevo estudiante
        Estudiante estudianteGuardado = estudianteRepository.save(nuevoEstudiante);

        // Agrega el nuevo estudiante a la lista y reordena por GPA
        estudiantesFacultad.add(estudianteGuardado);
        estudiantesFacultad.sort(Comparator.comparingDouble(Estudiante::getGpa).reversed());

        // Recalcula las posiciones en base al GPA
        for (int i = 0; i < estudiantesFacultad.size(); i++) {
            Estudiante estudiante = estudiantesFacultad.get(i);
            estudiante.setPosicionClasificacion(i + 1);
            estudianteRepository.save(estudiante);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(estudianteGuardado);
    }

    // 3. Actualizar el GPA de un estudiante y recalcular su clasificación
    @PutMapping("/{id}/actualizar-gpa")
    public ResponseEntity<?> actualizarGpa(@PathVariable Long id, @RequestBody Double nuevoGpa) {
        // Busca el estudiante por ID
        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(id);

        if (!estudianteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Estudiante no encontrado con el ID: " + id);
        }

        // Actualiza el GPA del estudiante
        Estudiante estudiante = estudianteOptional.get();
        estudiante.setGpa(nuevoGpa);
        estudianteRepository.save(estudiante);

        // Obtener la lista de estudiantes de la misma facultad, ordenada por GPA
        List<Estudiante> estudiantesFacultad = estudianteRepository.findByFacultadOrderByGpaDesc(estudiante.getFacultad());

        // Recalcula las posiciones basadas en el GPA
        for (int i = 0; i < estudiantesFacultad.size(); i++) {
            Estudiante est = estudiantesFacultad.get(i);
            est.setPosicionClasificacion(i + 1);
            estudianteRepository.save(est);
        }

        return ResponseEntity.ok("GPA actualizado y clasificación recalculada para el estudiante con ID: " + id);
    }

    // 4. Eliminar un estudiante y actualizar la clasificación
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstudiante(@PathVariable Long id) {
        // Verifica si el estudiante existe
        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(id);

        if (!estudianteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Estudiante no encontrado con el ID: " + id);
        }

        // Obtener la facultad del estudiante antes de eliminarlo
        String facultad = estudianteOptional.get().getFacultad();

        // Elimina el estudiante
        estudianteRepository.deleteById(id);

        // Obtener la lista de estudiantes de la misma facultad, ordenada por GPA
        List<Estudiante> estudiantesFacultad = estudianteRepository.findByFacultadOrderByGpaDesc(facultad);

        // Recalcula las posiciones basadas en el GPA
        for (int i = 0; i < estudiantesFacultad.size(); i++) {
            Estudiante est = estudiantesFacultad.get(i);
            est.setPosicionClasificacion(i + 1);
            estudianteRepository.save(est);
        }

        return ResponseEntity.ok("Estudiante eliminado y clasificación recalculada para la facultad: " + facultad);
    }
}
