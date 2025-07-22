package com.puskal.cameramedia.sound

data class ViewState(
    val audioFiles: List<String>? = null
)

sealed class SoundEvent
