package ru.cororo.songpay.data.response

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.http.ResponseEntity

data class ErrorResponse(
    val message: String,
    val code: Int,
    @JsonIgnore val httpCode: Int = 0,
) : StatusResponse("error")

fun ErrorResponse.toResponseEntity() = ResponseEntity.status(httpCode).body(this)
