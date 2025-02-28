package com.es.carlostareas.error

import com.es.carlostareas.error.exception.BadRequestException
import com.es.carlostareas.error.exception.UnauthorizedException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.mongodb.DuplicateKeyException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import javax.naming.AuthenticationException


@ControllerAdvice
class APIExceptionHandler {

    @ExceptionHandler(BadRequestException::class) 
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleBadRequest(request: HttpServletRequest, e: Exception) : ErrorRespuesta {
        e.printStackTrace()
        return ErrorRespuesta("Bad Request Exception (400).", e.message!!, request.requestURI)
    }

    @ExceptionHandler(DuplicateKeyException::class) 
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    fun handleDuplicate(request: HttpServletRequest, e: Exception) : ErrorRespuesta {
        e.printStackTrace()
        return ErrorRespuesta("Conflict (409).", e.message!!, request.requestURI)
    }

    @ExceptionHandler(AuthenticationException::class, UnauthorizedException::class) 
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    fun handleAuthentication(request: HttpServletRequest, e: Exception) : ErrorRespuesta {
        e.printStackTrace()
        return ErrorRespuesta("Unauthorized (409).", e.message!!, request.requestURI)
    }

    @ExceptionHandler(Exception::class, NullPointerException::class) 
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleGeneric(request: HttpServletRequest, e: Exception) : ErrorRespuesta {
        e.printStackTrace()
        return ErrorRespuesta("Internal Server Error (500)", e.message!!, request.requestURI)
    }
}