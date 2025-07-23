package com.puskal.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetDefaults
import androidx.compose.material3.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.puskal.theme.Black
import com.puskal.theme.Gray
import com.puskal.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiktokBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = White,
    contentColor: Color = Black,
    dragHandleColor: Color = Gray,
    dragHandle: @Composable (() -> Unit)? = { SheetDefaults.DragHandle(color = dragHandleColor) },
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        dragHandle = dragHandle,
        content = content
    )
}
