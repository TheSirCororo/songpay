package ru.cororo.songpay.data.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.cororo.songpay.data.user.model.User
import java.util.Optional

@Repository
interface UserRepo : JpaRepository<User, Long> {
    fun findByLoginIgnoreCaseOrEmailIgnoreCase(login: String, email: String): Optional<User>

    fun existsByLoginIgnoreCaseOrEmailIgnoreCase(login: String, email: String): Boolean
}
