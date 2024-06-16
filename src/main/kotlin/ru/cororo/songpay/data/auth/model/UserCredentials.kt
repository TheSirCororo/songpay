package ru.cororo.songpay.data.auth.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import ru.cororo.songpay.data.user.model.User
import java.time.Instant

@Entity
@Table(name = "user_credentials")
data class UserCredentials(
    @Id
    @Column(name = "user_id")
    val userId: Long,
    var hashedPassword: String,
    var lastChanged: Instant,
) {
    @[JsonIgnore OneToOne MapsId JoinColumn(name = "user_id")]
    lateinit var user: User
}
