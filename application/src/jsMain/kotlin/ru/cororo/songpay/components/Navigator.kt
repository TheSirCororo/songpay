package ru.cororo.songpay.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.stevdza.san.kotlinbs.components.BSNavBar
import com.stevdza.san.kotlinbs.models.BackgroundStyle
import com.stevdza.san.kotlinbs.models.navbar.NavBarButton
import com.stevdza.san.kotlinbs.models.navbar.NavLink
import com.varabyte.kobweb.browser.storage.BooleanStorageKey
import com.varabyte.kobweb.browser.storage.setItem
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.core.PageContext
import com.varabyte.kobweb.silk.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.localStorage

@Composable
fun Navigator(context: PageContext) {
    var colorMode by ColorMode.currentState

    BSNavBar(
        stickyTop = true,
        modifier = Modifier.fillMaxWidth()
            .then(SmoothColorStyle.toModifier()),
        items = listOf(
            NavLink(
                id = "test",
                title = "Test",
                onClick = {
                    context.router.navigateTo("/test")
                }
            ),
            NavLink(
                id = "main",
                title = "Main Page",
                onClick = {
                    context.router.navigateTo("/")
                }
            ),
        ),
        button = NavBarButton(
            id = "theme",
            text = "Change theme",
            onClick = {
                localStorage.setItem(BooleanStorageKey("dark-mode"), colorMode.opposite == ColorMode.DARK)
                colorMode = colorMode.opposite
            }
        ),
        backgroundStyle = if (colorMode.isLight) BackgroundStyle.Light else BackgroundStyle.Dark
    )
}