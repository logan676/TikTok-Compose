package com.puskal.cameramedia.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.puskal.cameramedia.edit.VideoFilter
import com.puskal.theme.White
import com.puskal.theme.Black
import com.puskal.theme.Gray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoFilterBottomSheet(
    currentFilter: VideoFilter,
    onSelectFilter: (VideoFilter) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = White,
        contentColor = Black,
        dragHandle = { SheetDefaults.DragHandle(color = Gray) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Choose filter",
                style = MaterialTheme.typography.titleMedium
            )
            VideoFilter.values().forEach { filter ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectFilter(filter) }
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = filter.title,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
