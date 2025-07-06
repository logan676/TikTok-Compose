package com.puskal.filter

import android.graphics.ColorMatrix

/**
 * Simple set of video filters with color matrices for preview.
 */
enum class VideoFilter(val title: String, val colorMatrix: ColorMatrix?) {
    NONE("None", null),
    GRAY("Gray", ColorMatrix().apply { setSaturation(0f) }),
    SEPIA("Sepia", ColorMatrix().apply { setScale(1f, 0.95f, 0.82f, 1f) }),
    INVERT("Invert", ColorMatrix(floatArrayOf(
        -1f, 0f, 0f, 0f, 255f,
        0f, -1f, 0f, 0f, 255f,
        0f, 0f, -1f, 0f, 255f,
        0f, 0f, 0f, 1f, 0f
    )));
}
