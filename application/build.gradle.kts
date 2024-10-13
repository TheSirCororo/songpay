@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("io.ktor.plugin") version "3.0.0"
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
}

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")
        }
    }
}

kotlin {
    configAsKobwebApplication("client", includeServer = true)

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
            implementation("io.konform:konform:0.7.0")
            implementation("io.insert-koin:koin-core:4.0.0")
            implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0")
        }

        jvmMain.dependencies {
            val ktorVersion = "3.0.0"
            val log4jVersion = "2.24.1"
            val exposedVersion = "0.55.0"
            val koinVersion = "4.0.0"
            val postgresVersion = "42.7.4"
            val serializationVersion = "1.7.3"
            implementation("io.ktor:ktor-server-netty:$ktorVersion")
            implementation("io.ktor:ktor-server-request-validation:$ktorVersion")
            implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
            implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
            implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
            implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
            implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
            implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
            implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4jVersion")
            runtimeOnly("org.postgresql:postgresql:$postgresVersion")
            implementation(project.dependencies.platform("io.insert-koin:koin-bom:$koinVersion"))
            implementation("io.insert-koin:koin-ktor")
            implementation("io.insert-koin:koin-logger-slf4j")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            implementation(libs.compose.runtime)
            compileOnly(libs.kobweb.api) // Provided by Kobweb backend at runtime
        }

        jsMain.dependencies {
            val ktorVersion = "3.0.0"
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.silk.icons.fa)
            implementation("io.ktor:ktor-client-core:$ktorVersion")
            implementation("io.ktor:ktor-client-js:$ktorVersion")
            implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
            implementation("io.insert-koin:koin-compose:4.0.0")
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
//
//    js {
//        browser {
//            runTask {
//                mainOutputFileName = "main.bundle.js"
//                sourceMaps = false
//            }
//            webpackTask {
//                mainOutputFileName = "main.bundle.js"
//            }
//        }
//        binaries.executable()
//    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
}