package com.puskal.cameramedia.edit

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import com.puskal.theme.TikTokTheme
import com.puskal.theme.R
import com.puskal.cameramedia.edit.TimelineEditor

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoTrimScreen(
    videoUri: String,
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    TikTokTheme(darkTheme = true) {
        var selectedTool by remember { mutableStateOf(TrimTool.TRIM) }
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(id = R.string.trim)) },
                    navigationIcon = {
                        IconButton(onClick = onCancel) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.cancel),
                                tint = Color.Unspecified
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onSave) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = stringResource(id = R.string.save),
                                tint = Color.Unspecified
                            )
                        }
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

            modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clickable { isPlaying = !isPlaying }
                ) {
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
                        modifier = Modifier.fillMaxSize()
                    )
                }

                TimelineEditor(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
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
