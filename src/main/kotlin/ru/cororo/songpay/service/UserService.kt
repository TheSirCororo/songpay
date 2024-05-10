package ru.cororo.songpay.service

import org.springframework.stereotype.Service
import ru.cororo.songpay.data.settings.repository.UserSettingsRepo
import ru.cororo.songpay.data.user.model.User
import ru.cororo.songpay.data.user.repository.UserRepo
import ru.cororo.songpay.data.user.request.UpdateMeRequest

@Service
class UserService(
    private val userRepo: UserRepo,
    private val userSettingsRepo: UserSettingsRepo
) {
    fun updateUser(user: User, request: UpdateMeRequest): User {
        user.settings.displayName = request.displayName
        userSettingsRepo.save(user.settings)
        return user
    }
}