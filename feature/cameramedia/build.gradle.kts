plugins {
    id("plugin.android-common")
}


dependencies {
    COMMON_THEME
    COMMON_COMPOSABLE
    DOMAIN
    DATA
    CORE
    FEATURE_FILTER
    cameraXDependencies()
    media3Dependency()
    implementation(project(":common:videotrimmer"))
    implementation(project(":feature:cameracapture"))
}