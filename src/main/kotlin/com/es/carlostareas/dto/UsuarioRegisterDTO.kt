package com.es.carlostareas.dto

import com.es.carlostareas.model.Direccion

data class UsuarioRegisterDTO(
    val username: String,
    val email: String,
    val password: String,
    val passwordRepeat: String,
    val rol: String?,
    val direccion: Direccion
)
