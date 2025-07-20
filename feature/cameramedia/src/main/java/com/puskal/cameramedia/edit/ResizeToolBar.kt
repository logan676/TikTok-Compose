package com.puskal.cameramedia.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ResizeToolBar(
    modifier: Modifier = Modifier,
    onFeatureSelected: (ResizeMenuFeature) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ResizeMenuFeature.values().forEach { feature ->
            Image(
                painter = painterResource(id = feature.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onFeatureSelected(feature) },
                alignment = Alignment.Center
            )
        }
    }
}
