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
            // AssetManager.list returns an array. Convert it to a List so that the
            // null coalescing expression keeps a consistent type and we can
            // safely use collection operations like `map`.
            val files: List<String> =
                context.assets.list("audios")?.toList()?.sorted() ?: emptyList()
            updateState(ViewState(audioFiles = files.map { it.substringBeforeLast('.') }))
        }
    }

    override fun onTriggerEvent(event: SoundEvent) {}
}
