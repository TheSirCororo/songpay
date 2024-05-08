package ru.cororo.songpay.data.response

class StatusCodeException(override val message: String, val errorResponse: ErrorResponse) : RuntimeException(message)

fun respondError(response: ErrorResponse): Nothing = throw StatusCodeException(response.message, response)
