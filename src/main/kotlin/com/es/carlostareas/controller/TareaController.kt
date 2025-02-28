package com.es.carlostareas.controller

import com.es.carlostareas.dto.TareaInsertDTO
import com.es.carlostareas.model.Tarea
import com.es.carlostareas.service.TareaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tareas")
class TareaController {

    @Autowired
    private lateinit var tareaService: TareaService

    @GetMapping
    fun obtenerTareas(): ResponseEntity<List<Tarea>> {
        return ResponseEntity(tareaService.obtenerTareas(), HttpStatus.OK)
    }

    @PostMapping
    fun crearTarea(
        @RequestBody tarea: TareaInsertDTO
    ): ResponseEntity<Tarea> {
        val tareaC = tareaService.crearTarea(tarea)
        return ResponseEntity(tareaC, HttpStatus.CREATED)
    }

    @PutMapping("/{tareaId}/completar")
    fun marcarComoHecha(@PathVariable tareaId: String): ResponseEntity<Any> {
        tareaService.marcarComoHecha(tareaId)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/{tareaId}")
    fun eliminarTarea(@PathVariable tareaId: String): ResponseEntity<Any> {
        tareaService.eliminarTarea(tareaId)
        return ResponseEntity(HttpStatus.OK)
    }
}
