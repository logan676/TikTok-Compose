package com.puskal.cameramedia.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image

@Composable
fun VideoEditToolBar(
    modifier: Modifier = Modifier,
    onToolSelected: (VideoEditTool) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VideoEditTool.values().forEach { tool ->
            Image(
                painter = painterResource(id = tool.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onToolSelected(tool) },
                alignment = Alignment.Center
            )
        }
    }
}
