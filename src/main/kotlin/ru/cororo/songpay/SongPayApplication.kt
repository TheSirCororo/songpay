package ru.cororo.songpay

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.cororo.songpay.config.ApplicationConfig

fun main(args: Array<String>) {
    runApplication<SongPayApplication>(*args)
}

@ImportAutoConfiguration(ApplicationConfig::class)
@SpringBootApplication
class SongPayApplication
