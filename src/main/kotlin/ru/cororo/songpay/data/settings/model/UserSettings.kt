package ru.cororo.songpay.data.settings.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import ru.cororo.songpay.data.user.model.User

@Entity
@Table(name = "user_settings")
data class UserSettings(
    @Id
    @Column(name = "user_id")
    var userId: Long = 0,
    @Column(name = "display_name")
    var displayName: String,
) {
    @[JsonIgnore OneToOne MapsId JoinColumn(name = "user_id")]
    lateinit var user: User
}
