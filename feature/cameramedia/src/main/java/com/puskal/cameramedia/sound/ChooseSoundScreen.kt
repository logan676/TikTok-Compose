package com.puskal.cameramedia.sound

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.puskal.composable.TopBar
import com.puskal.core.DestinationRoute
import com.puskal.data.model.AudioModel
import com.puskal.theme.R
import com.puskal.theme.SubTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseSoundScreen(
    navController: NavController,
    viewModel: ChooseSoundViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    Scaffold(topBar = {
        TopBar(title = stringResource(id = R.string.sounds)) {
            navController.navigateUp()
        }
    }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            viewState?.audios?.let { list ->
                items(list) { audio ->
                    AudioRow(audio)
                }
            }
        }
    }
}

@Composable
private fun AudioRow(audio: AudioModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = "file:///android_asset/templateimages/${'$'}{audio.audioCoverImage}",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(6.dp))
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = 12.dp)) {
            val title = if (audio.isOriginal) {
                stringResource(id = R.string.original_sound_of, audio.audioAuthor.fullName)
            } else audio.audioAuthor.fullName
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = stringResource(id = R.string.number_posts, audio.numberOfPost),
                style = MaterialTheme.typography.labelSmall,
                color = SubTextColor
            )
        }
        TextButton(onClick = { }) {
            Text(text = stringResource(id = R.string.use_this_sound))
        }
    }
}
