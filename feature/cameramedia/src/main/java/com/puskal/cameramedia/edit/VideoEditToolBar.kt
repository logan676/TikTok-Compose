package com.puskal.cameramedia.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon

@Composable
fun VideoEditToolBar(
    modifier: Modifier = Modifier,
    showFilters: Boolean = true,
    expanded: Boolean = false,
    onToolSelected: (VideoEditTool) -> Unit = {},
    onFeatureSelected: (ResizeMenuFeature) -> Unit = {},
    onToggleExpand: () -> Unit = {}
) {
    val collapsedTools = remember(showFilters) {
        listOf(
            VideoEditTool.SETTINGS,
            VideoEditTool.SHARE,
            VideoEditTool.TRIM,
            VideoEditTool.TEXT,
            VideoEditTool.CHALLENGE,
            VideoEditTool.STICKERS,
            VideoEditTool.EFFECTS
        )
    }
    val extraTools = listOf(
        VideoEditTool.FILTERS,
        VideoEditTool.BEAUTY,
        VideoEditTool.CROP_RESIZE
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = if (expanded) modifier.verticalScroll(scrollState) else modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        collapsedTools.forEach { tool ->
            Icon(
                imageVector = tool.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onToolSelected(tool) }
            )
        }

        if (expanded) {
            extraTools.forEach { tool ->
                if (showFilters || tool != VideoEditTool.FILTERS) {
                    Icon(
                        imageVector = tool.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onToolSelected(tool) }
                    )
                }
            }
            ResizeMenuFeature.values().forEach { feature ->
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            if (feature == ResizeMenuFeature.COLLAPSE_TOOLBAR) onToggleExpand()
                            else onFeatureSelected(feature)
                        }
                )
            }
        } else {
            Icon(
                imageVector = VideoEditTool.MORE.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onToggleExpand() }
            )
        }
    }
}
