package com.puskal.cameramedia.edit

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.puskal.theme.R
import com.puskal.theme.TikTokTheme
import com.redevrx.video_trimmer.event.OnVideoEditedEvent
import com.redevrx.video_trimmer.view.VideoEditor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoTrimScreen(
    videoUri: String,
    onCancel: () -> Unit = {},
    onSave: (String) -> Unit = {}
) {
    TikTokTheme(darkTheme = true) {
        val context = LocalContext.current
        var selectedTool by remember { mutableStateOf(TrimTool.TRIM) }
        var saveRequested by remember { mutableStateOf(false) }
        var isSaving by remember { mutableStateOf(false) }
        val editorRef = remember { mutableStateOf<VideoEditor?>(null) }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(id = R.string.trim)) },
                    navigationIcon = {
                        IconButton(onClick = {
                            editorRef.value?.onCancelClicked()
                            onCancel()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.cancel),
                                tint = Color.Unspecified
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { saveRequested = true }) {
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
            AndroidView(
                factory = { ctx ->
                    VideoEditor(ctx).apply {
                        setDestinationPath(ctx.cacheDir.absolutePath)
                        setVideoURI(Uri.parse(videoUri))
                        setOnTrimVideoListener(object : OnVideoEditedEvent {
                            override fun getResult(uri: Uri) {
                                isSaving = false
                                onSave(uri.toString())
                            }

                            override fun onError(message: String) {
                                isSaving = false
                            }
                        })
                        editorRef.value = this
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) { view ->
                editorRef.value = view
                if (saveRequested) {
                    saveRequested = false
                    isSaving = true
                    view.saveVideo()
                }
            }

            if (isSaving) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
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
