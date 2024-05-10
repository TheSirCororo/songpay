package ru.cororo.songpay.data.settings.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.cororo.songpay.data.settings.model.UserSettings

@Repository
interface UserSettingsRepo : JpaRepository<UserSettings, Long>