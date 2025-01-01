package com.tareas.gestion_tareas.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tareas.gestion_tareas.entity.TareaEntity;
import com.tareas.gestion_tareas.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private ObjectMapper objectMapper;

    // Crear una nueva tarea
    @PostMapping
    public ResponseEntity<TareaEntity> createTarea(@RequestBody TareaEntity tarea) {
        return ResponseEntity.ok(tareaService.saveTarea(tarea));
    }

    // Obtener una tarea por ID
    @GetMapping("/{idTarea}")
    public ResponseEntity<TareaEntity> getTareaById(@PathVariable Long idTarea) {
        Optional<TareaEntity> tarea = tareaService.getTareaById(idTarea);
        return tarea.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Obtener todas las tareas
    @GetMapping
    public ResponseEntity<List<TareaEntity>> getAllTareas() {
        return ResponseEntity.ok(tareaService.getAllTareas());
    }

    // Actualizar una tarea existente
    @PutMapping("/{idTarea}")
    public ResponseEntity<TareaEntity> updateTarea(@PathVariable Long idTarea, @RequestBody TareaEntity tarea) {
        Optional<TareaEntity> existingTarea = tareaService.getTareaById(idTarea);
        if (existingTarea.isPresent()) {
            tarea.setIdTarea(idTarea);
            return ResponseEntity.ok(tareaService.saveTarea(tarea));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar una tarea por ID
    @DeleteMapping("/{idTarea}")
    public ResponseEntity<Void> deleteTarea(@PathVariable Long idTarea) {
        tareaService.deleteTarea(idTarea);
        return ResponseEntity.noContent().build();
    }

    // Actualizar el estado de una tarea
    @PatchMapping("/{idTarea}/estado")
    public ResponseEntity<TareaEntity> updateEstadoTarea(@PathVariable Long idTarea, @RequestParam TareaEntity.EstadoTarea nuevoEstado) {
        Optional<TareaEntity> tarea = tareaService.updateEstadoTarea(idTarea, nuevoEstado);
        return tarea.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para cargar un archivo JSON y guardar las tareas en la base de datos
    @PostMapping("/upload")
    public String uploadTareasFile(@RequestParam("file") MultipartFile file) {
        try {
            // Convertir el archivo JSON a una lista de objetos TareaEntity
            List<TareaEntity> tarea = objectMapper.readValue(file.getInputStream(), objectMapper.getTypeFactory().constructCollectionType(List.class, TareaEntity.class));

            // Guardar las tareas en la base de datos
            tareaService.saveTareas(tarea);

            return "Archivo procesado y tareas guardadas con éxito.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al procesar el archivo.";
        }
    }
}