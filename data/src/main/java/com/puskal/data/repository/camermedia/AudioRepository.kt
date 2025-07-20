package com.puskal.data.repository.camermedia

import com.puskal.data.model.AudioModel
import com.puskal.data.source.AudioDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AudioRepository @Inject constructor() {
    fun getAudios(): Flow<List<AudioModel>> {
        return AudioDataSource.fetchAudios()
    }
}
