package com.puskal.cameramedia.sound

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.puskal.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseSoundViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseViewModel<ViewState, SoundEvent>() {

    init {
        fetchAudios()
    }

    private fun fetchAudios() {
        viewModelScope.launch {
            val files = context.assets.list("audios")?.sorted() ?: emptyArray()
            updateState(ViewState(audioFiles = files.map { it.substringBeforeLast('.') }))
        }
    }

    override fun onTriggerEvent(event: SoundEvent) {}
}
