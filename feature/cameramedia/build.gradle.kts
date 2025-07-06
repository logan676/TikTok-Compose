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
    implementation(project(":feature:cameracapture"))
}