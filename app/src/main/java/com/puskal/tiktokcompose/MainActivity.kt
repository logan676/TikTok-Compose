package com.puskal.tiktokcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.net.Uri
import com.puskal.core.DestinationRoute
import com.puskal.tiktokcompose.BuildConfig
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val debugStart = if (BuildConfig.DEBUG) {
            val assetUri = "asset:///videos/jeremy_vid1.mp4"
            DestinationRoute.VIDEO_EDIT_ROUTE + "/" + Uri.encode(assetUri)
        } else {
            null
        }

        setContent {
            RootScreen(startDestination = debugStart ?: DestinationRoute.HOME_SCREEN_ROUTE)
        }
    }
}


private object Destinations {
    const val Home = "HOME"
    const val Feed = "FEED"
    const val Sheet = "SHEET"
}


