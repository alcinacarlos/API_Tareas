package com.es.carlostareas.model

import org.bson.codecs.pojo.annotations.BsonId
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document("Tarea")
data class Tarea(
    @BsonId
    val _id: String?,
    val titulo: String,
    val descripcion: String,
    val usuarioId: String, // Relación con Usuºario
    val completada: Boolean = false,
    val fechaCreaccion: LocalDate = LocalDate.now()
)
