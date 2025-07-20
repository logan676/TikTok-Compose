package com.puskal.cameramedia

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.puskal.data.model.TemplateModel
import com.puskal.theme.R

/**
 * Created by Puskal Khadka on 4/3/2023.
 */
data class ViewState(
    val templates: List<TemplateModel>? = null
)

sealed class CameraMediaEvent {
    object EventFetchTemplate : CameraMediaEvent()

}

enum class Tabs(@StringRes val rawValue: Int) {
    CAMERA(rawValue = R.string.camera),
    STORY(rawValue = R.string.story),
    TEMPLATES(rawValue = R.string.templates)
}

enum class PermissionType {
    CAMERA,
    MICROPHONE
}

enum class CameraController(
    @StringRes val title: Int,
    val icon: ImageVector
) {
    FLIP(title = R.string.flip, icon = ImageVector.vectorResource(R.drawable.ic_flip)),
    SPEED(title = R.string.speed, icon = ImageVector.vectorResource(R.drawable.ic_speed)),
    BEAUTY(title = R.string.beauty, icon = ImageVector.vectorResource(R.drawable.ic_profile_fill)),
    FILTER(title = R.string.filters, icon = Icons.Filled.Filter),
    MIRROR(title = R.string.mirror, icon = ImageVector.vectorResource(R.drawable.ic_mirror)),
    TIMER(title = R.string.timer, icon = ImageVector.vectorResource(R.drawable.ic_timer)),
    FLASH(title = R.string.flash, icon = ImageVector.vectorResource(R.drawable.ic_flash)),
}

enum class CameraCaptureOptions(val value: String) {
    SIXTY_SECOND("60s"),
    FIFTEEN_SECOND("15s"),
    PHOTO("Photo"),
    VIDEO("Video"),
    TEXT("Text"),
}

