package com.puskal.cameramedia.sound

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.puskal.core.base.BaseViewModel
import com.puskal.domain.cameramedia.GetAudioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseSoundViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getAudioUseCase: GetAudioUseCase
) : BaseViewModel<ViewState, SoundEvent>() {

    init {
        fetchAudios()
    }

    private fun fetchAudios() {
        viewModelScope.launch {
            getAudioUseCase().collect {
                updateState(ViewState(audioFiles = it))
            }
        }
    }

    override fun onTriggerEvent(event: SoundEvent) {}
}
