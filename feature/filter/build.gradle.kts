plugins {
    id("plugin.android-common")
}


dependencies {
    COMMON_THEME
    COMMON_COMPOSABLE
    DOMAIN
    DATA
    CORE
    implementation(project(":feature:mp4compose"))
}
