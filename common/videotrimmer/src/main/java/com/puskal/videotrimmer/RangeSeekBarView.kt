package com.puskal.videotrimmer

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.slider.RangeSlider

/**
 * Simple RangeSeekBarView backed by [RangeSlider].
 */
class RangeSeekBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : RangeSlider(context, attrs) {

    init {
        // We want values between 0f and 100f by default
        valueFrom = 0f
        valueTo = 100f
        setValues(0f, 100f)
    }
}
