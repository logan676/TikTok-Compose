package com.puskal.cameramedia.edit

import android.net.Uri
import android.view.ViewGroup
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
import androidx.media3.ui.PlayerView
import androidx.media3.ui.AspectRatioFrameLayout
import com.puskal.theme.R
import com.puskal.theme.TikTokTheme
import com.puskal.theme.White
import androidx.compose.ui.unit.dp
import com.puskal.cameramedia.MusicBarLayout
import com.puskal.cameramedia.edit.VideoFilter
import com.puskal.cameramedia.edit.GlFilterPlayerView
import com.puskal.cameramedia.edit.VideoFilterBottomSheet

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoEditScreen(
    videoUri: String,
    onClickBack: () -> Unit,
    onTrimVideo: (String) -> Unit = {},
    enableFilters: Boolean = false
) {
    TikTokTheme(darkTheme = true) {
        Scaffold { padding ->
            val context = LocalContext.current
            var showResizeMenu by remember { mutableStateOf(false) }
            var showFilterSheet by remember(enableFilters) { mutableStateOf(false) }
            var selectedFilter by remember(enableFilters) { mutableStateOf(VideoFilter.NONE) }
            val exoPlayer = remember(videoUri) {
                // Initialize the player but delay preparing it until the surface is ready
                ExoPlayer.Builder(context).build()
            }
            DisposableEffect(exoPlayer) { onDispose { exoPlayer.release() } }

            // Flag used to make sure the player is only prepared once when the
            // rendering surface becomes available
            var isPlayerPrepared by remember { mutableStateOf(false) }

            val playerView = if (enableFilters) {
                remember { GlFilterPlayerView(context) }
            } else {
                remember {
                    PlayerView(context).apply {
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                }
            }
            DisposableEffect(playerView) {
                onDispose {
                    when (playerView) {
                        is GlFilterPlayerView -> playerView.player = null
                        is PlayerView -> playerView.player = null
                    }
                }
            }
            LaunchedEffect(exoPlayer, playerView) {
                when (playerView) {
                    is GlFilterPlayerView -> playerView.player = exoPlayer
                    is PlayerView -> playerView.player = exoPlayer
                }
            }
            if (enableFilters) {
                LaunchedEffect(selectedFilter) {
                    (playerView as GlFilterPlayerView).setGlFilter(selectedFilter.create())
                }
            }

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                AndroidView(
                    factory = { playerView },
                    modifier = Modifier.fillMaxSize(),
                    update = { view ->
                        when (view) {
                            is GlFilterPlayerView -> {
                                view.player = exoPlayer
                                if (!isPlayerPrepared && view.isAvailable) {
                                    exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
                                    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                                    exoPlayer.prepare()
                                    exoPlayer.playWhenReady = true
                                    isPlayerPrepared = true
                                }
                            }
                            is PlayerView -> {
                                view.player = exoPlayer
                                if (!isPlayerPrepared) {
                                    exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
                                    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
                                    exoPlayer.prepare()
                                    exoPlayer.playWhenReady = true
                                    isPlayerPrepared = true
                                }
                            }
                        }
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
                    showFilters = enableFilters,
                    onToolSelected = {
                        when (it) {
                            VideoEditTool.CROP_RESIZE -> {
                                showResizeMenu = !showResizeMenu
                            }
                            VideoEditTool.TRIM -> onTrimVideo(videoUri)
                            VideoEditTool.FILTERS -> if (enableFilters) showFilterSheet = true
                            else -> {}
                        }
                    }
                )

                if (enableFilters && showFilterSheet) {
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
