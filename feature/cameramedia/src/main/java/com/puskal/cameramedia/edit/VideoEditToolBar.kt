package com.puskal.cameramedia.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon

@Composable
fun VideoEditToolBar(
    modifier: Modifier = Modifier,
    showFilters: Boolean = true,
    onToolSelected: (VideoEditTool) -> Unit = {}
) {
    val tools = remember(showFilters) {
        if (showFilters) {
            VideoEditTool.values().toList()
        } else {
            VideoEditTool.values().filterNot { it == VideoEditTool.FILTERS }
        }
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        tools.forEach { tool ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = tool.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onToolSelected(tool) }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = tool.title),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }
    }
}
