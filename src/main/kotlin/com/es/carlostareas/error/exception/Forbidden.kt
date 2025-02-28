package com.es.carlostareas.error.exception

class Forbidden(message: String) : Exception("Forbidden (403). $message") {
}