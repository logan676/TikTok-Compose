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
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share

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
            val iconModifier = Modifier
                .size(32.dp)
                .clickable { onToolSelected(tool) }
            when (tool) {
                VideoEditTool.SETTINGS -> Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = null,
                    modifier = iconModifier
                )
                VideoEditTool.SHARE -> Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = null,
                    modifier = iconModifier
                )
                VideoEditTool.TRIM -> Icon(
                    imageVector = Icons.Filled.ContentCut,
                    contentDescription = null,
                    modifier = iconModifier
                )
                else -> Icon(
                    painter = painterResource(id = tool.iconRes!!),
                    contentDescription = null,
                    modifier = iconModifier
                )
            }
        }
    }
}
