package com.es.carlostareas

import com.es.carlostareas.security.RSAKeysProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(RSAKeysProperties::class)
class AplicacionTareas()

fun main(args: Array<String>) {
	runApplication<AplicacionTareas>(*args)
}
