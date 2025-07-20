package com.puskal.cameramedia.edit

import androidx.annotation.DrawableRes
import com.puskal.theme.R

enum class VideoEditTool(@DrawableRes val iconRes: Int? = null) {
    SETTINGS(),
    SHARE(),
    TRIM(),
    TEXT(R.drawable.ic_mention),
    CHALLENGE(R.drawable.ic_flag),
    STICKERS(R.drawable.ic_emoji),
    EFFECTS(R.drawable.ic_wallpaper),
    FILTERS(R.drawable.ic_filter),
    BEAUTY(R.drawable.ic_profile_fill),
    CROP_RESIZE(R.drawable.ic_arrow_down)
}
