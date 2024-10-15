package ru.cororo.songpay.components

import androidx.compose.runtime.Composable
import com.stevdza.san.kotlinbs.forms.BSInput
import com.stevdza.san.kotlinbs.models.InputValidation
import com.varabyte.kobweb.compose.ui.Modifier
import org.jetbrains.compose.web.attributes.InputType

@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    validate: ((String) -> Boolean)? = null,
    getValue: () -> String,
    inputHandler: (String) -> Unit = {}
) {
    val validateResult = validate?.invoke(getValue())
    BSInput(
        type = InputType.Text,
        value = getValue(),
        onValueChange = inputHandler,
        label = placeholder,
        placeholder = placeholder,
        validation = if (validateResult != null) InputValidation(isValid = validateResult, isInvalid = !validateResult, validFeedback = "Корректно!", invalidFeedback = "Некорректно!") else InputValidation(),
        floating = true,
        modifier = modifier
    )
}
