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
import androidx.compose.material3.LinearProgressIndicator
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
import com.puskal.videotrimmer.VideoEditor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoTrimScreen(
    videoUri: String,
    onCancel: () -> Unit = {},
    onSave: (String) -> Unit = {}
) {
    TikTokTheme(darkTheme = true) {
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

        var isPlaying by remember { mutableStateOf(true) }
        var startMs by remember { mutableLongStateOf(0L) }
        var endMs by remember { mutableLongStateOf(0L) }
        var isSaving by remember { mutableStateOf(false) }
        var progress by remember { mutableStateOf(0) }
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
                        IconButton(onClick = {
                            if (!isSaving) {
                                isSaving = true
                                saveVideo(
                                    context = context,
                                    inputUri = Uri.parse(videoUri),
                                    startMs = startMs,
                                    endMs = endMs,
                                    onProgress = { progress = it },
                                    onSave = { uri ->
                                        isSaving = false
                                        uri?.let { onSave(it.toString()) }
                                    }
                                )
                            }
                        }) {
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
            LaunchedEffect(isPlaying) {
                if (isPlaying) exoPlayer.play() else exoPlayer.pause()
            }

            Column(
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
                                setUseTextureView(true)
                                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                AndroidView(
                    factory = { ctx ->
                        VideoEditor(ctx).apply {
                            setVideoUri(Uri.parse(videoUri))
                            onRangeChanged = { start, end ->
                                startMs = start
                                endMs = end
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    update = { view ->
                        view.onRangeChanged = { s, e ->
                            startMs = s
                            endMs = e
                        }
                    }
                )

                if (isSaving) {
                    LinearProgressIndicator(
                        progress = progress / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
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

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
private fun saveVideo(
    context: android.content.Context,
    inputUri: Uri,
    startMs: Long,
    endMs: Long,
    onProgress: (Int) -> Unit = {},
    onSave: (Uri?) -> Unit
) {
    val output = java.io.File(context.cacheDir, "trim_${'$'}{System.currentTimeMillis()}.mp4")
    val mediaItem = MediaItem.Builder()
        .setUri(inputUri)
        .setClippingConfiguration(
            MediaItem.ClippingConfiguration.Builder()
                .setStartPositionMs(startMs)
                .setEndPositionMs(endMs)
                .build()
        )
        .build()
    val edited = androidx.media3.transformer.EditedMediaItem.Builder(mediaItem).build()
    val transformer = androidx.media3.transformer.Transformer.Builder(context)
        .addListener(object : androidx.media3.transformer.Transformer.Listener {
            override fun onCompleted(
                composition: androidx.media3.transformer.Composition,
                exportResult: androidx.media3.transformer.ExportResult
            ) {
                onSave(Uri.fromFile(output))
            }

            override fun onError(
                composition: androidx.media3.transformer.Composition,
                exportResult: androidx.media3.transformer.ExportResult,
                exportException: androidx.media3.transformer.ExportException
            ) {
                exportException.printStackTrace()
                onSave(null)
            }
        })
        .build()

    transformer.start(edited, output.absolutePath)

    kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.Default) {
        val holder = androidx.media3.transformer.ProgressHolder()
        while (true) {
            when (transformer.getProgress(holder)) {
                androidx.media3.transformer.Transformer.PROGRESS_STATE_AVAILABLE -> onProgress(holder.progress)
                androidx.media3.transformer.Transformer.PROGRESS_STATE_NOT_STARTED -> break
            }
            kotlinx.coroutines.delay(100)
        }
    }
}
