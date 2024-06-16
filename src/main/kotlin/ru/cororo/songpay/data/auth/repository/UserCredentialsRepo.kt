package ru.cororo.songpay.data.auth.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.cororo.songpay.data.auth.model.UserCredentials

@Repository
interface UserCredentialsRepo : JpaRepository<UserCredentials, Long>
