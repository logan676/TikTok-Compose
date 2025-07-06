package com.puskal.cameramedia.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.puskal.filter.VideoFilter

@Composable
fun FilterBottomSheet(
    currentFilter: VideoFilter,
    onSelectFilter: (VideoFilter) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
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
