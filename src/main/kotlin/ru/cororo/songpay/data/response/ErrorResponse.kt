package ru.cororo.songpay.data.response

import org.springframework.http.ResponseEntity

data class ErrorResponse(val message: String, val code: Int, @Transient val httpCode: Int)

fun ErrorResponse.toResponseEntity() = ResponseEntity.status(httpCode).body(this)
