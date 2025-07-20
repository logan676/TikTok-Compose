package com.puskal.cameramedia

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.puskal.core.DestinationRoute
import com.puskal.core.DestinationRoute.FORMATTED_VIDEO_EDIT_ROUTE
import com.puskal.core.DestinationRoute.PassedKey
import com.puskal.cameramedia.edit.VideoEditScreen

/**
 * Created by Puskal Khadka on 4/2/2023.
 */
fun NavGraphBuilder.cameraMediaNavGraph(navController: NavController) {
    composable(route = DestinationRoute.CAMERA_ROUTE) {
        CameraMediaScreen(navController)
    }
    composable(route = DestinationRoute.CHOOSE_SOUND_ROUTE) {
        com.puskal.cameramedia.sound.ChooseSoundScreen(navController)
    }
    composable(
        route = FORMATTED_VIDEO_EDIT_ROUTE,
        arguments = listOf(navArgument(PassedKey.VIDEO_URI) { type = NavType.StringType })
    ) { backStackEntry ->
        val uri = backStackEntry.arguments?.getString(PassedKey.VIDEO_URI) ?: ""
        VideoEditScreen(videoUri = uri) { navController.navigateUp() }
    }
}