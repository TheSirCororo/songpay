package ru.cororo.songpay.common.auth.request

import io.konform.validation.Validation
import kotlinx.serialization.Serializable
import ru.cororo.songpay.common.core.validation.email
import ru.cororo.songpay.common.core.validation.login
import ru.cororo.songpay.common.core.validation.password
import ru.cororo.songpay.common.core.validation.validator

@Serializable
data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
) {
    companion object : Validation<RegisterRequest> by validator({
        RegisterRequest::email {
            email()
        }
        RegisterRequest::username {
            login()
        }
        RegisterRequest::password {
            password()
        }
    })
}
