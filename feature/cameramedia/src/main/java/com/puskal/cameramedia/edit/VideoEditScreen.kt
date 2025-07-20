package com.puskal.cameramedia.edit

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.puskal.theme.R
import com.puskal.theme.TikTokTheme
import com.puskal.theme.White
import androidx.compose.ui.unit.dp
import com.puskal.cameramedia.MusicBarLayout
import com.puskal.filter.VideoFilter
import com.puskal.cameramedia.edit.GlFilterPlayerView
import com.puskal.cameramedia.edit.VideoFilterBottomSheet

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoEditScreen(
    videoUri: String,
    onClickBack: () -> Unit,
    onTrimVideo: (String) -> Unit = {}
) {
    TikTokTheme(darkTheme = true) {
        Scaffold { padding ->
            val context = LocalContext.current
            var showResizeMenu by remember { mutableStateOf(false) }
            var showFilterSheet by remember { mutableStateOf(false) }
            var selectedFilter by remember { mutableStateOf(VideoFilter.NONE) }
            val exoPlayer = remember(videoUri) {
                ExoPlayer.Builder(context).build().apply {
                    repeatMode = Player.REPEAT_MODE_ONE
                    setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
                    playWhenReady = true
                    prepare()
                }
            }
            DisposableEffect(exoPlayer) { onDispose { exoPlayer.release() } }

            val filterPlayerView = remember { GlFilterPlayerView(context) }
            DisposableEffect(filterPlayerView) { onDispose { filterPlayerView.player = null } }
            LaunchedEffect(exoPlayer) { filterPlayerView.player = exoPlayer }
            LaunchedEffect(selectedFilter) {
                filterPlayerView.setGlFilter(selectedFilter.create())
            }

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                AndroidView(
                    factory = { filterPlayerView },
                    modifier = Modifier.fillMaxSize(),
                    update = { view ->
                        view.player = exoPlayer
                    }
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = White,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 16.dp, top = 32.dp)
                        .size(24.dp)
                        .clickable { onClickBack() }
                )

                MusicBarLayout(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 32.dp),
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
                        when (it) {
                            VideoEditTool.CROP_RESIZE -> {
                                showResizeMenu = !showResizeMenu
                            }
                            VideoEditTool.TRIM -> onTrimVideo(videoUri)
                            VideoEditTool.FILTERS -> showFilterSheet = true
                            else -> {}
                        }
                    }
                )

                if (showFilterSheet) {
                    VideoFilterBottomSheet(
                        currentFilter = selectedFilter,
                        onSelectFilter = {
                            selectedFilter = it
                            showFilterSheet = false
                        },
                        onDismiss = { showFilterSheet = false }
                    )
                }
            }
        }
    }
}
