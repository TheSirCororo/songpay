package ru.cororo.songpay.data.user.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.cororo.songpay.data.auth.model.UserCredentials
import ru.cororo.songpay.data.settings.model.UserSettings

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "login", unique = true)
    val login: String,
    @Column(name = "email", unique = true)
    val email: String,
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    @PrimaryKeyJoinColumn
    @JsonIgnore
    val credentials: UserCredentials,
    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    @PrimaryKeyJoinColumn
    val settings: UserSettings,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableSetOf(SimpleGrantedAuthority("USER"))

    override fun getPassword(): String = credentials.hashedPassword

    override fun getUsername(): String = login

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
