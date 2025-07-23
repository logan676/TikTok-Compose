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
            audioCoverImage = "img1.jpg",
            isOriginal = true,
            audioAuthor = charliePuth,
            numberOfPost = 239000,
            duration = "02:10",
            originalVideoUrl = "charlieputh_vid1.mp4"
        ),
        AudioModel(
            audioCoverImage = "img2.jpg",
            isOriginal = true,
            audioAuthor = kylieJenner,
            numberOfPost = 42000,
            duration = "03:20",
            originalVideoUrl = "kylie_vid2.mp4"
        ),
        AudioModel(
            audioCoverImage = "img3.jpg",
            isOriginal = true,
            audioAuthor = duaLipa,
            numberOfPost = 120340,
            duration = "01:45",
            originalVideoUrl = "dua_vid1.mp4"
        ),
        AudioModel(
            audioCoverImage = "img4.jpg",
            isOriginal = true,
            audioAuthor = taylor,
            numberOfPost = 15200,
            duration = "02:58",
            originalVideoUrl = "taylor_vid1.mp4"
        )
    )

    fun fetchAudios(): Flow<List<AudioModel>> = flow { emit(audioList) }
}
