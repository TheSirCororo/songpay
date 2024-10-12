@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("io.ktor.plugin") version "3.0.0"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            implementation("io.konform:konform:0.7.0")
        }

        jvmMain.dependencies {
            val ktorVersion = "3.0.0"
            val log4jVersion = "2.24.1"
            val exposedVersion = "0.55.0"
            val koinVersion = "4.0.0"
            val postgresVersion = "42.7.4"
            implementation("io.ktor:ktor-server-netty:$ktorVersion")
            implementation("io.ktor:ktor-server-request-validation:$ktorVersion")
            implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
            implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
            implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
            implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
            implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
            runtimeOnly("org.postgresql:postgresql:$postgresVersion")
            implementation(project.dependencies.platform("io.insert-koin:koin-bom:$koinVersion"))
            implementation("io.insert-koin:koin-core")
            implementation("io.insert-koin:koin-ktor")
            implementation("io.insert-koin:koin-logger-slf4j")
        }
    }

    jvm {
        apply(plugin = "io.ktor.plugin")

        ktor {
            docker {
                jreVersion.set(JavaVersion.VERSION_21)
            }
        }
    }

    js {
        browser()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
}