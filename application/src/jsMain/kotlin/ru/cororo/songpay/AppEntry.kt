package ru.cororo.songpay

import androidx.compose.runtime.*
import com.varabyte.kobweb.browser.storage.BooleanStorageKey
import com.varabyte.kobweb.browser.storage.getItem
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.button
import com.varabyte.kobweb.silk.theme.colors.palette.color
import com.varabyte.kobweb.silk.theme.colors.palette.input
import com.varabyte.kobweb.silk.theme.colors.palette.link
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.*
import org.koin.compose.KoinApplication
import org.koin.dsl.module

private val appModule = module {
    single<HttpClient> {
        HttpClient(Js) {
            install(ContentNegotiation) {
                json()
            }
        }
    }
}

@InitSilk
fun updateTheme(ctx: InitSilkContext) {
    ctx.theme.palettes.light.apply {
        background = Colors.White
        color = Colors.Black
        link.default = Colors.Black
        link.visited = Colors.Blue
        button.default = Colors.DarkGray
        button.hover = Colors.LightGray
        button.focus = Colors.LightGray
        button.pressed = Colors.Gray
        input.filled = Colors.White
    }

    ctx.theme.palettes.dark.apply {
        background = Colors.Black
        color = Colors.White
        link.default = Colors.White
        link.visited = Colors.Blue
        button.default = Colors.Blue
        button.hover = Colors.MediumBlue
        button.focus = Colors.MediumBlue
        button.pressed = Colors.DarkBlue
    }

    ctx.config.initialColorMode =
        if (localStorage.getItem(BooleanStorageKey("dark-mode", true)) != false) ColorMode.DARK else ColorMode.LIGHT
}

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    KoinApplication({
        modules(appModule)
    }) {
        SilkApp {
            Surface(SmoothColorStyle.toModifier().minHeight(100.vh)) {
                content()
            }
        }
    }
}
