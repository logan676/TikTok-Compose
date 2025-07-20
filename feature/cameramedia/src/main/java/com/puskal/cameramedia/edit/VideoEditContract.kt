package com.puskal.cameramedia.edit

import androidx.annotation.DrawableRes
import com.puskal.theme.R

enum class VideoEditTool(@DrawableRes val icon: Int) {
    SETTINGS(R.drawable.ic_hamburger),
    SHARE(R.drawable.ic_share),
    TRIM(R.drawable.ic_delete),
    TEXT(R.drawable.ic_mention),
    CHALLENGE(R.drawable.ic_flag),
    STICKERS(R.drawable.ic_emoji),
    EFFECTS(R.drawable.ic_wallpaper),
    FILTERS(R.drawable.ic_filter),
    BEAUTY(R.drawable.ic_profile_fill),
    CROP_RESIZE(R.drawable.ic_arrow_down)
}

enum class ResizeMenuFeature(@DrawableRes val icon: Int) {
    AUTO_SUBTITLES(R.drawable.ic_auto_subtitles),
    QUALITY_ENHANCEMENT(R.drawable.ic_quality_enhance),
    VOICE_CHANGER(R.drawable.ic_microphone),
    BRUSH_TOOL(R.drawable.ic_brush_tool),
    COLLAPSE_TOOLBAR(R.drawable.ic_arrow_down)
}
