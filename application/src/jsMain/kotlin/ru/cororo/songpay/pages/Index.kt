package ru.cororo.songpay.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.Transition
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.icons.fa.FaFaceSmile
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.icons.fa.IconStyle
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.selectors.focus
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgb
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.koin.compose.koinInject
import ru.cororo.songpay.common.auth.request.RegisterRequest

@Page
@Composable
fun HomePage(client: HttpClient = koinInject()) {
    var name by remember { mutableStateOf("") }
    var colorMode by ColorMode.currentState
    var response by remember { mutableStateOf("") }
    ColorMode

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FaFaceSmile(
                modifier = Modifier.margin(right = 10.px)
                    .color(if (colorMode.isLight) Colors.Black else Theme.Blue.color),
                size = IconSize.XXL,
                style = IconStyle.FILLED
            )

            P(
                attrs = Modifier
                    .fontSize(40.px)
                    .toAttrs()
            ) {
                Text("Hello, $name!")
            }
        }

        Input(
            type = InputType.Text,
            attrs = InputStyle.toModifier()
                .padding(topBottom = 10.px, leftRight = 20.px)
                .fontSize(20.px)
                .backgroundColor(Theme.Gray.color)
                .outline(
                    width = 0.px,
                    style = LineStyle.None,
                    color = Colors.Transparent
                )
                .transition(Transition.of(property = "border", duration = 300.ms))
                .toAttrs {
                    placeholder("Enter your name")
                    onInput { name = it.value }
                }
        )

        Button(
            modifier = Modifier.margin(top = 24.px),
            onClick = {
                colorMode = colorMode.opposite
            }
        ) {
            Text("Change Theme")
        }

        LaunchedEffect(Unit) {
            println("Running request...")
            val request = RegisterRequest("artem.cororo@gmail.com", "Cororo", "12345678aA")
            println("Running request... x2 $request")
            response = client.post("https://localhost:5000/api/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.bodyAsText()
        }

        Text("Response: $response")
    }
}

val InputStyle = CssStyle {
    base {
        Modifier.border(
            width = 2.px,
            style = LineStyle.Solid,
            color = Theme.Gray.color
        )
    }

    focus {
        Modifier.border(
            width = 2.px,
            style = LineStyle.Solid,
            color = Theme.Blue.color
        )
    }
}

enum class Theme(val color: CSSColorValue) {
    Gray(color = rgb(238, 238, 238)),
    Blue(color = rgb(28, 181, 224)),
}
