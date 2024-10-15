package ru.cororo.songpay.pages.test

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import org.jetbrains.compose.web.dom.Text
import ru.cororo.songpay.components.Navigator

@Page
@Composable
fun TestPage() {
    val pageContext = rememberPageContext()


    Column(Modifier.fillMaxSize()) {
        Navigator(pageContext)

        Row(Modifier.fillMaxWidth()) {
            Text("Test text")
        }
    }
}