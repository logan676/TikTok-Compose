package com.puskal.composable

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A convenience wrapper around [CustomButton] that uses a large rounded shape.
 */
@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClickButton: () -> Unit,
) {
    CustomButton(
        modifier = modifier,
        buttonText = buttonText,
        shape = RoundedCornerShape(percent = 50),
        style = MaterialTheme.typography.labelLarge,
        onClickButton = onClickButton
    )
}
