plugins {
    kotlin("multiplatform") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.multiplatform")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    group = "ru.cororo"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    kotlin {
        jvm()
        js {
            browser()
        }
    }
}