package ru.cororo.songpay.common.auth.request

import io.konform.validation.Validation
import kotlinx.serialization.Serializable
import ru.cororo.songpay.common.core.validation.login
import ru.cororo.songpay.common.core.validation.password
import ru.cororo.songpay.common.core.validation.validator

@Serializable
data class SignInRequest(
    val login: String,
    val password: String
) {
    companion object : Validation<SignInRequest> by validator({
        SignInRequest::login {
            login()
        }
        SignInRequest::password {
            password()
        }
    })
}
