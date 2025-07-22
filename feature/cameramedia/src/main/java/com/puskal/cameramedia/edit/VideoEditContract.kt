package com.puskal.cameramedia.edit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.ArStickers
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Subtitles
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

enum class VideoEditTool(val icon: ImageVector) {
    SETTINGS(Icons.Filled.Settings),
    SHARE(Icons.Filled.Share),
    TRIM(Icons.Filled.ContentCut),
    TEXT(Icons.Filled.TextFields),
    CHALLENGE(Icons.Filled.Flag),
    STICKERS(Icons.Filled.ArStickers),
    EFFECTS(Icons.Filled.Wallpaper),
    FILTERS(Icons.Filled.InvertColors),
    BEAUTY(Icons.Filled.Face),
    CROP_RESIZE(Icons.Filled.Crop)
}

enum class ResizeMenuFeature(val icon: ImageVector) {
    AUTO_SUBTITLES(Icons.Outlined.Subtitles),
    QUALITY_ENHANCEMENT(Icons.Outlined.Star),
    VOICE_CHANGER(Icons.Outlined.Mic),
    BRUSH_TOOL(Icons.Outlined.Brush),
    COLLAPSE_TOOLBAR(Icons.Filled.KeyboardArrowDown)
}
