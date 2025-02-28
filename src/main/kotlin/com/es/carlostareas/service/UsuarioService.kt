package com.es.carlostareas.service

import com.es.carlostareas.dto.UsuarioDTO
import com.es.carlostareas.dto.UsuarioRegisterDTO
import com.es.carlostareas.error.exception.BadRequestException
import com.es.carlostareas.error.exception.UnauthorizedException
import com.es.carlostareas.model.Usuario
import com.es.carlostareas.repository.UsuarioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import utils.Utils

@Service
class UsuarioService : UserDetailsService {

    @Autowired
    private lateinit var usuarioRepository: UsuarioRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var externalApiService: ExternalApiService


    override fun loadUserByUsername(username: String?): UserDetails {
        val usuario: Usuario = usuarioRepository
            .findByUsername(username!!)
            .orElseThrow {
                UnauthorizedException("$username no existente")
            }

        return User.builder()
            .username(usuario.username)
            .password(usuario.password)
            .roles(usuario.roles)
            .build()
    }

    fun insertUser(usuarioInsertadoDTO: UsuarioRegisterDTO) : UsuarioDTO {


        if (usuarioInsertadoDTO.username.isBlank()
            || usuarioInsertadoDTO.email.isBlank()
            || usuarioInsertadoDTO.password.isBlank()
            || usuarioInsertadoDTO.passwordRepeat.isBlank()) {

            throw BadRequestException("Uno o más campos vacíos")
        }

        // Comprobar email válido
        if (!Utils.isEmailValid(usuarioInsertadoDTO.email)) {
            throw BadRequestException("Email ${usuarioInsertadoDTO.email} incorrecto")
        }
        // Comprobar si el usuario ya existe
        if(usuarioRepository.findByUsername(usuarioInsertadoDTO.username).isPresent) {
            throw BadRequestException("Usuario ${usuarioInsertadoDTO.username} ya está registrado")
        }

        // Comprobar que ambas passwords sean iguales
        if(usuarioInsertadoDTO.password != usuarioInsertadoDTO.passwordRepeat) {
            throw BadRequestException("Las contrasenias no coinciden")
        }

        // Comprobar el rol
        if(usuarioInsertadoDTO.rol != null && usuarioInsertadoDTO.rol != "USER" && usuarioInsertadoDTO.rol != "ADMIN" ) {
            throw BadRequestException("ROL: ${usuarioInsertadoDTO.rol} incorrecto")
        }

        // Comprobar el email
        if(usuarioRepository.findByEmail(usuarioInsertadoDTO.email).isPresent) {
            throw BadRequestException("Usuario ${usuarioInsertadoDTO.email} ya está registrado")
        }

        // Comprobar la provincia
        val datosProvincias = externalApiService.obtenerProvinciasDesdeApi()
        var cpro: String = ""
        if(datosProvincias != null) {
            if(datosProvincias.data != null) {
                val provinciaEncontrada = datosProvincias.data.stream().filter {
                    it.PRO == usuarioInsertadoDTO.direccion.provincia.uppercase()
                }.findFirst().orElseThrow {
                    BadRequestException("Provincia ${usuarioInsertadoDTO.direccion.provincia} no encontrada")
                }
                cpro = provinciaEncontrada.CPRO
            }
        }

        // Comprobar el municipio
        val datosMunicipios = externalApiService.obtenerMunicipiosDesdeApi(cpro)
        if(datosMunicipios != null) {
            if(datosMunicipios.data != null) {
                datosMunicipios.data.stream().filter {
                    it.DMUN50 == usuarioInsertadoDTO.direccion.municipio.uppercase()
                }.findFirst().orElseThrow {
                    BadRequestException("Municipio ${usuarioInsertadoDTO.direccion.municipio} no se encuentra en ${usuarioInsertadoDTO.direccion.provincia}")
                }
            }
        }

        val usuario = Usuario(
             null,
            usuarioInsertadoDTO.username,
            passwordEncoder.encode(usuarioInsertadoDTO.password),
            usuarioInsertadoDTO.email,
            usuarioInsertadoDTO.rol,
            usuarioInsertadoDTO.direccion
        )

        usuarioRepository.insert(usuario)

        return UsuarioDTO(
            usuario.username,
            usuario.email,
            usuario.roles
        )

    }
}