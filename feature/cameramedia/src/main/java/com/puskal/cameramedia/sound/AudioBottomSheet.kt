package com.puskal.cameramedia.sound

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import android.net.Uri
import androidx.hilt.navigation.compose.hiltViewModel
import com.puskal.theme.GrayMainColor
import com.puskal.theme.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioBottomSheet(
    onDismiss: () -> Unit,
    viewModel: ChooseSoundViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    var search by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    var playingItem by remember { mutableStateOf<String?>(null) }
    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 36.dp, height = 4.dp)
                        .background(color = GrayMainColor, shape = RoundedCornerShape(2.dp))
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null
                    )
                },
                placeholder = { Text(text = stringResource(id = R.string.search)) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val tabs = listOf(
                    stringResource(id = R.string.recommended),
                    stringResource(id = R.string.hot),
                    stringResource(id = R.string.favorites),
                    stringResource(id = R.string.used)
                )
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.weight(1f)
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(text = title) }
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp, start = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = stringResource(id = R.string.import_audio))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f, fill = false)
            ) {
                viewState?.audioFiles?.let { list ->
                    items(list) { audio ->
                        AudioRow(
                            name = audio,
                            isPlaying = playingItem == audio,
                            onClick = {
                                if (playingItem == audio && exoPlayer.isPlaying) {
                                    exoPlayer.stop()
                                    playingItem = null
                                } else {
                                    val uri = "file:///android_asset/audios/${audio}.mp3"
                                    exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(uri)))
                                    exoPlayer.prepare()
                                    exoPlayer.playWhenReady = true
                                    playingItem = audio
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomAction(text = stringResource(id = R.string.original_sound), icon = R.drawable.ic_music_note)
                BottomAction(text = stringResource(id = R.string.music_off), icon = R.drawable.ic_music_off)
                BottomAction(text = stringResource(id = R.string.volume), icon = R.drawable.ic_volume)
                BottomAction(text = stringResource(id = R.string.subtitle), icon = R.drawable.ic_auto_subtitles)
            }
        }
    }
}

@Composable
private fun BottomAction(text: String, icon: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(painter = painterResource(id = icon), contentDescription = text, tint = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = text, style = MaterialTheme.typography.labelSmall, color = Color.White)
    }
}

@Composable
private fun AudioRow(name: String, isPlaying: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_music_note),
            contentDescription = null,
            modifier = Modifier.size(56.dp),
            tint = if (isPlaying) MaterialTheme.colorScheme.primary else Color.White
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        TextButton(onClick = { }) {
            Text(text = stringResource(id = R.string.use_this_sound))
        }
    }
}
