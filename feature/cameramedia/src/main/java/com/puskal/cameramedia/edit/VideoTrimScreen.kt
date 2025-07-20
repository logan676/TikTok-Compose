package com.puskal.cameramedia.edit

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.puskal.theme.TikTokTheme
import com.puskal.theme.R

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoTrimScreen(
    videoUri: String,
    onBack: () -> Unit = {}
) {
    TikTokTheme(darkTheme = true) {
        var selectedTool by remember { mutableStateOf(TrimTool.TRIM) }
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(id = R.string.trim)) },
                    navigationIcon = {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { onBack() }
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                TrimBottomBar(selectedTool = selectedTool) { selectedTool = it }
            }
        ) { padding ->
            val context = LocalContext.current
            val exoPlayer = remember(videoUri) {
                ExoPlayer.Builder(context).build().apply {
                    repeatMode = Player.REPEAT_MODE_ONE
                    setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
                    playWhenReady = true
                    prepare()
                }
            }
            DisposableEffect(exoPlayer) { onDispose { exoPlayer.release() } }

            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        player = exoPlayer
                        useController = false
                        // Use a TextureView so the video layers correctly with other Compose UI
                        useTextureView = true
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
        }
    }
}

@Composable
private fun TrimBottomBar(
    modifier: Modifier = Modifier,
    selectedTool: TrimTool,
    onToolSelected: (TrimTool) -> Unit
) {
    NavigationBar(modifier = modifier) {
        TrimTool.values().forEach { tool ->
            NavigationBarItem(
                selected = selectedTool == tool,
                onClick = { onToolSelected(tool) },
                icon = {
                    Icon(imageVector = tool.icon, contentDescription = null, tint = Color.White)
                }
            )
        }
    }
}
