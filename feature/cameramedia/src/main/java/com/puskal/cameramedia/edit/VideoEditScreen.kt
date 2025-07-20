package com.puskal.cameramedia.edit

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.puskal.theme.R
import com.puskal.theme.TikTokTheme
import androidx.compose.ui.unit.dp
import com.puskal.cameramedia.MusicBarLayout

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoEditScreen(
    videoUri: String,
    onClickBack: () -> Unit
) {
    TikTokTheme(darkTheme = true) {
        Scaffold { padding ->
            val context = LocalContext.current
            var showResizeMenu by remember { mutableStateOf(false) }
            val exoPlayer = remember(videoUri) {
                ExoPlayer.Builder(context).build().apply {
                    repeatMode = Player.REPEAT_MODE_ONE
                    setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
                    playWhenReady = true
                    prepare()
                }
            }
            DisposableEffect(exoPlayer) { onDispose { exoPlayer.release() } }

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 16.dp, top = 16.dp)
                        .size(24.dp)
                        .clickable { onClickBack() }
                )
                AndroidView(
                    factory = {
                        PlayerView(it).apply {
                            player = exoPlayer
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                MusicBarLayout(
                    modifier = Modifier.align(Alignment.TopCenter),
                    onClickAddSound = {}
                )

                if (showResizeMenu) {
                    ResizeToolBar(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 64.dp)
                    )
                }

                VideoEditToolBar(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp),
                    onToolSelected = {
                        if (it == VideoEditTool.CROP_RESIZE) {
                            showResizeMenu = !showResizeMenu
                        }
                    }
                )
            }
        }
    }
}
