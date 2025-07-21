package com.puskal.cameramedia.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.puskal.data.model.AudioModel
import com.puskal.cameramedia.sound.ChooseSoundViewModel
import com.puskal.theme.R
import com.puskal.theme.SubTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseAudioBottomSheet(
    onSelectAudio: (AudioModel) -> Unit,
    onDismiss: () -> Unit,
    viewModel: ChooseSoundViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.sounds),
                style = MaterialTheme.typography.titleMedium
            )
            viewState?.audios?.forEach { audio ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelectAudio(audio)
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = audio.parseCoverImage(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {
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
                    TextButton(onClick = { onSelectAudio(audio) }) {
                        Text(text = stringResource(id = R.string.use_this_sound))
                    }
                }
            }
        }
    }
}

