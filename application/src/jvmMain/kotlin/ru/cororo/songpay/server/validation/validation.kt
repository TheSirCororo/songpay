package ru.cororo.songpay.server.validation

import io.konform.validation.Invalid
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import ru.cororo.songpay.common.core.response.StatusResponse
import ru.cororo.songpay.common.core.validation.validate

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<Any> { data ->
            val result = data.validate()
            if (result != null && result is Invalid) {
                ValidationResult.Invalid(result.errors.map { "${data::class.simpleName}${it.dataPath}: ${it.message}" })
            } else if (result == null) {
                log.debug("Validator for {} was not found.", data)
                ValidationResult.Valid
            } else {
                ValidationResult.Valid
            }
        }
    }

    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, StatusResponse.Error(cause.reasons.joinToString(", "), 1))
        }
    }
}