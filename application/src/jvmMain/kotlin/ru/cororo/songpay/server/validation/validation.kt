package ru.cororo.songpay.server.validation

import io.konform.validation.Invalid
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import ru.cororo.songpay.common.core.validation.validate

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<Any> {
            val result = it.validate()
            if (result != null && result is Invalid) {
                ValidationResult.Invalid(result.errors.map { "${it.dataPath}: ${it.message}" })
            } else if (result == null) {
                log.debug("Validator for {} was not found.", it)
                ValidationResult.Valid
            } else {
                ValidationResult.Valid
            }
        }
    }
}