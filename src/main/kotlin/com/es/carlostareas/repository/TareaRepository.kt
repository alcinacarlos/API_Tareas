package com.es.carlostareas.repository

import com.es.carlostareas.model.Tarea
import org.springframework.data.mongodb.repository.MongoRepository

interface TareaRepository: MongoRepository<Tarea, String> {
    fun findByUsuarioId(usuarioId: String): List<Tarea>
}