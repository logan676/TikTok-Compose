package com.puskal.cameramedia.sound

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.puskal.composable.TopBar
import com.puskal.core.DestinationRoute
import com.puskal.data.model.AudioModel
import com.puskal.theme.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseSoundScreen(
    navController: NavController,
    viewModel: ChooseSoundViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    Column(
        modifier = Modifier.fillMaxHeight(0.96f)
    ) {
        TopBar(navIcon = R.drawable.ic_cancel, title = stringResource(id = R.string.sounds)) {
            navController.navigateUp()
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            viewState?.audioFiles?.let { list ->
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
        Icon(
            painter = painterResource(id = R.drawable.ic_music_note),
            contentDescription = null,
            modifier = Modifier.size(56.dp)
        )
        Text(
            text = audio.audioAuthor.fullName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        )
        TextButton(onClick = { }) {
            Text(text = stringResource(id = R.string.use_this_sound))
        }
    }
}
