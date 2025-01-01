package com.tareas.gestion_tareas.service;

import com.tareas.gestion_tareas.entity.TareaEntity;
import com.tareas.gestion_tareas.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    // Crear o actualizar una tarea
    public TareaEntity saveTarea(TareaEntity tarea) {
        return tareaRepository.save(tarea);
    }

    // Obtener una tarea por ID
    public Optional<TareaEntity> getTareaById(Long idTarea) {
        return tareaRepository.findById(idTarea);
    }

    // Obtener todas las tareas
    public List<TareaEntity> getAllTareas() {
        return tareaRepository.findAll();
    }

    // Eliminar una tarea por ID
    public void deleteTarea(Long idTarea) {
        tareaRepository.deleteById(idTarea);
    }

    // Actualizar el estado de una tarea
    public Optional<TareaEntity> updateEstadoTarea(Long idTarea, TareaEntity.EstadoTarea nuevoEstado) {
        Optional<TareaEntity> tarea = tareaRepository.findById(idTarea);
        if (tarea.isPresent()) {
            TareaEntity tareaExistente = tarea.get();
            tareaExistente.setEstadoTarea(nuevoEstado);
            return Optional.of(tareaRepository.save(tareaExistente));
        }
        return Optional.empty();
    }

    // Método para guardar varias tareas a la vez
    public void saveTareas(List<TareaEntity> tareas) {
        tareaRepository.saveAll(tareas);  // Guardar todas las tareas de una vez
    }
}