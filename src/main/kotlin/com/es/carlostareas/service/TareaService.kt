package com.es.carlostareas.service


import com.es.carlostareas.error.exception.BadRequestException
import com.es.carlostareas.error.exception.UnauthorizedException
import com.es.carlostareas.model.Tarea
import com.es.carlostareas.repository.TareaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class TareaService {

    @Autowired
    private lateinit var tareaRepository: TareaRepository

    fun obtenerTareas(): List<Tarea> {
        val auth = SecurityContextHolder.getContext().authentication
        return if (auth.authorities.any { it.authority == "ROLE_ADMIN" }) {
            tareaRepository.findAll()
        } else {
            tareaRepository.findByUsuarioId(auth.name)
        }
    }

    fun crearTarea(titulo: String, descripcion: String, usuarioId: String?): Tarea {
        val auth = SecurityContextHolder.getContext().authentication
        val esAdmin = auth.authorities.any { it.authority == "ROLE_ADMIN" }

        val idUsuarioAsignado = usuarioId ?: auth.name
        if (!esAdmin && usuarioId != null && usuarioId != auth.name) {
            throw UnauthorizedException("No puedes asignar tareas a otro usuario")
        }

        val tarea = Tarea(
            _id = null,
            titulo = titulo,
            descripcion = descripcion,
            usuarioId = idUsuarioAsignado,
            completada = false
        )
        return tareaRepository.save(tarea)
    }

    fun marcarComoHecha(tareaId: String) {
        val auth = SecurityContextHolder.getContext().authentication
        val tarea = tareaRepository.findById(tareaId).orElseThrow { BadRequestException("Tarea no encontrada") }

        if (tarea.usuarioId != auth.name) {
            throw UnauthorizedException("No puedes marcar como hecha una tarea que no es tuya")
        }

        tareaRepository.save(tarea.copy(completada = true))
    }

    fun eliminarTarea(tareaId: String) {
        val auth = SecurityContextHolder.getContext().authentication
        val esAdmin = auth.authorities.any { it.authority == "ROLE_ADMIN" }
        val tarea = tareaRepository.findById(tareaId).orElseThrow { BadRequestException("Tarea no encontrada") }

        if (!esAdmin && tarea.usuarioId != auth.name) {
            throw UnauthorizedException("No puedes eliminar una tarea que no es tuya")
        }

        tareaRepository.deleteById(tareaId)
    }
}