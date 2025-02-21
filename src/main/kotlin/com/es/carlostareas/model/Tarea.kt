package com.es.carlostareas.model

import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document

@Document("Tarea")
data class Tarea(
    @BsonId
    val _id: String?,
    val titulo: String,
    val descripcion: String,
    val usuarioId: String, // Relación con Usuario
    val completada: Boolean = false
)
