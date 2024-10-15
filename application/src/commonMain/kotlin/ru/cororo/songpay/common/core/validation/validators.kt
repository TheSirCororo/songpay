package ru.cororo.songpay.common.core.validation

import io.konform.validation.ValidationBuilder
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern

internal inline fun <reified T : Any> validator(noinline builder: ValidationBuilder<T>.() -> Unit) =
    ValidationManager.registerValidator(builder)

//language=RegExp
internal fun ValidationBuilder<String>.email() =
    pattern("^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])\$")

//language=RegExp
internal fun ValidationBuilder<String>.login() {
    minLength(5)
    maxLength(15)
    pattern("^[a-zA-Z0-9_]+$")
}

private val lowercaseLetters = 'a'..'z'
private val uppercaseLetters = 'A'..'Z'
private val digits = '0'..'9'

internal fun ValidationBuilder<String>.password() {
    minLength(8)
    maxLength(128)
    addConstraint("Password must have lowercase and uppercase latin letters, digits", "aAbcd123b") {
        it.any { it in lowercaseLetters } && it.any { it in uppercaseLetters} && it.any { it in digits }
    }
}
