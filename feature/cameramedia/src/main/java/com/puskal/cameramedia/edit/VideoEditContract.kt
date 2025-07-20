package com.puskal.cameramedia.edit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.annotation.DrawableRes
import com.puskal.theme.R

enum class VideoEditTool(val icon: ImageVector) {
    SETTINGS(Icons.Filled.Settings),
    SHARE(Icons.Filled.Share),
    TRIM(Icons.Filled.ContentCut),
    TEXT(Icons.Filled.TextFields),
    CHALLENGE(Icons.Filled.Flag),
    STICKERS(Icons.Filled.EmojiEmotions),
    EFFECTS(Icons.Filled.Wallpaper),
    FILTERS(Icons.Filled.Filter),
    BEAUTY(Icons.Filled.Face),
    CROP_RESIZE(Icons.Filled.Crop)
}

enum class ResizeMenuFeature(@DrawableRes val icon: Int) {
    AUTO_SUBTITLES(R.drawable.ic_auto_subtitles),
    QUALITY_ENHANCEMENT(R.drawable.ic_quality_enhance),
    VOICE_CHANGER(R.drawable.ic_microphone),
    BRUSH_TOOL(R.drawable.ic_brush_tool),
    COLLAPSE_TOOLBAR(R.drawable.ic_arrow_down)
}
