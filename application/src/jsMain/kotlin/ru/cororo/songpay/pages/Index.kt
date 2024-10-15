package ru.cororo.songpay.pages

import androidx.compose.runtime.*
import com.stevdza.san.kotlinbs.components.BSAlert
import com.stevdza.san.kotlinbs.models.AlertIcon
import com.stevdza.san.kotlinbs.models.AlertStyle
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.icons.fa.FaFaceSmile
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.icons.fa.IconStyle
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.rgb
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.koin.compose.koinInject
import ru.cororo.songpay.common.auth.request.RegisterRequest
import ru.cororo.songpay.components.Navigator
import ru.cororo.songpay.components.TextInput

@Page
@Composable
fun HomePage(client: HttpClient = koinInject()) {
    var name by remember { mutableStateOf("") }
    var colorMode by ColorMode.currentState
    var response by remember { mutableStateOf("") }
    var serverError by remember { mutableStateOf(false) }
    val pageContext = rememberPageContext()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Navigator(pageContext)

        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
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

            TextInput(
                placeholder = "Enter your name",
                getValue = { name },
                inputHandler = { name = it },
                validate = { it == "Cororo" }
            )

            if (serverError) {
                BSAlert(
                    message = "Произошла ошибка сервера!",
                    icon = AlertIcon.Warning,
                    dismissible = true,
                    style = AlertStyle.Danger
                )
            }

            LaunchedEffect(Unit) {
                println("Running request...")
                val request = RegisterRequest("artem.cororo@gmail.com", "Cororo", "12345678aA")
                println("Running request... x2 $request")
                response = try {
                    client.post("http://localhost:5000/api/auth/register") {
                        contentType(ContentType.Application.Json)
                        setBody(request)
                    }.bodyAsText()
                } catch (e: Throwable) {
                    e.printStackTrace()
                    serverError = true
                    "Произошла ошибка при запросе к серверу!"
                }
            }

            Text("Response: $response")
        }
    }
}

enum class Theme(val color: CSSColorValue) {
    Gray(color = rgb(238, 238, 238)),
    Blue(color = rgb(28, 181, 224)),
}
