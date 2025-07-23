package com.puskal.data.source

import com.puskal.data.model.AudioModel
import com.puskal.data.source.UsersDataSource.charliePuth
import com.puskal.data.source.UsersDataSource.kylieJenner
import com.puskal.data.source.UsersDataSource.duaLipa
import com.puskal.data.source.UsersDataSource.taylor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object AudioDataSource {
    private val audioList = listOf(
        AudioModel(
            audioCoverImage = "cover_1.jpg",
            isOriginal = true,
            audioAuthor = charliePuth,
            numberOfPost = 239000,
            duration = "02:10",
            originalVideoUrl = "audio1.mp3"
        ),
        AudioModel(
            audioCoverImage = "cover_2.jpg",
            isOriginal = true,
            audioAuthor = kylieJenner,
            numberOfPost = 42000,
            duration = "03:20",
            originalVideoUrl = "audio2.mp3"
        ),
        AudioModel(
            audioCoverImage = "cover_3.jpg",
            isOriginal = true,
            audioAuthor = duaLipa,
            numberOfPost = 120340,
            duration = "01:45",
            originalVideoUrl = "audio3.mp3"
        ),
        AudioModel(
            audioCoverImage = "cover_4.jpg",
            isOriginal = true,
            audioAuthor = taylor,
            numberOfPost = 15200,
            duration = "02:58",
            originalVideoUrl = "audio4.mp3"
        )
    )

    fun fetchAudios(): Flow<List<AudioModel>> = flow { emit(audioList) }
}
