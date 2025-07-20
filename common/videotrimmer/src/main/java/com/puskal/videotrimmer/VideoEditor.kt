package com.puskal.videotrimmer

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.puskal.videotrimmer.R

/**
 * Simple video editor view that lets user select trim range using [RangeSeekBarView].
 */
@OptIn(UnstableApi::class)
class VideoEditor @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val playerView: PlayerView
    private val rangeView: RangeSeekBarView
    private var player: ExoPlayer? = null
    private var durationMs: Long = 0

    var onRangeChanged: ((Long, Long) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.trimmer_view_layout, this, true)
        playerView = PlayerView(context).also { it.useController = false }
        addView(playerView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        rangeView = findViewById(R.id.rangeSlider)
        rangeView.addOnChangeListener { slider, _, _ ->
            val start = slider.values.getOrNull(0)?.toLong() ?: 0L
            val end = slider.values.getOrNull(1)?.toLong() ?: durationMs.toLong()
            onRangeChanged?.invoke(start, end)
        }
    }

    fun setVideoUri(uri: Uri) {
        player?.release()
        player = ExoPlayer.Builder(context).build().also { exoPlayer ->
            exoPlayer.setMediaItem(MediaItem.fromUri(uri))
            exoPlayer.prepare()
        }
        playerView.player = player
        player?.addListener(object : androidx.media3.common.Player.Listener {
            override fun onTimelineChanged(timeline: androidx.media3.common.Timeline, reason: Int) {
                durationMs = player?.duration ?: 0
                rangeView.valueTo = durationMs.toFloat()
                rangeView.values = listOf(0f, durationMs.toFloat())
            }
        })
    }

    fun release() {
        playerView.player = null
        player?.release()
        player = null
    }
}
