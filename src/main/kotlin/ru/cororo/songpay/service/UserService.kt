package ru.cororo.songpay.service

import org.springframework.stereotype.Service
import ru.cororo.songpay.data.user.model.User
import ru.cororo.songpay.data.user.repository.UserRepo
import ru.cororo.songpay.data.user.request.UpdateMeRequest

@Service
class UserService(private val userRepo: UserRepo) {
    fun updateUser(user: User, request: UpdateMeRequest): User {
        user.displayName = request.displayName
        return userRepo.save(user)
    }
}