
import AppConfig

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("jacoco")
}

android {
    namespace = "com.puskal." + project.name.replace("-", "")
    compileSdk = AppConfig.compileSdk
    defaultConfig {
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["filter"] = "" +
                "com.otaliastudios.cameraview.tools.SdkExcludeFilter," +
                "com.otaliastudios.cameraview.tools.SdkIncludeFilter"
    }
    buildTypes["debug"].isTestCoverageEnabled = true
    buildTypes["release"].isMinifyEnabled = false
}

dependencies {
    testImplementation("junit:junit:4.13.1")
    testImplementation("org.mockito:mockito-inline:2.28.2")

    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("org.mockito:mockito-android:2.28.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    api("androidx.exifinterface:exifinterface:1.3.3")
    api("androidx.lifecycle:lifecycle-common:2.3.1")
    api("com.google.android.gms:play-services-tasks:17.2.1")
    implementation("androidx.annotation:annotation:1.2.0")
    implementation("com.otaliastudios.opengl:egloo:0.6.1")
    implementation(Libraries.Google.material)
}

// Publishing


// Code Coverage
val buildDir = project.buildDir.absolutePath
val coverageInputDir = "$buildDir/coverage_input" // changing? change github workflow
val coverageOutputDir = "$buildDir/coverage_output" // changing? change github workflow

// Run unit tests, with coverage enabled in the android { } configuration.
// Output will be an .exec file in build/jacoco.
tasks.register("runUnitTests") { // changing name? change github workflow
    dependsOn("testDebugUnitTest")
    doLast {
        copy {
            from("$buildDir/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
            into("$coverageInputDir/unit_tests") // changing? change github workflow
        }
    }
}

// Run android tests with coverage.
tasks.register("runAndroidTests") { // changing name? change github workflow
    dependsOn("connectedDebugAndroidTest")
    doLast {
        copy {
            from("$buildDir/outputs/code_coverage/debugAndroidTest/connected")
            include("*coverage.ec")
            into("$coverageInputDir/android_tests") // changing? change github workflow
        }
    }
}

// Merge the two with a jacoco task.
jacoco { toolVersion = "0.8.5" }
tasks.register("computeCoverage", JacocoReport::class) {
    dependsOn("compileDebugSources") // Compile sources, needed below
    executionData.from(fileTree(coverageInputDir))
    sourceDirectories.from(android.sourceSets["main"].java.srcDirs)
    additionalSourceDirs.from("$buildDir/generated/source/buildConfig/debug")
    additionalSourceDirs.from("$buildDir/generated/source/r/debug")
    classDirectories.from(fileTree("$buildDir/intermediates/javac/debug") {
        // Not everything here is relevant for CameraView, but let's keep it generic
        exclude(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "android/**",
            "androidx/**",
            "com/google/**",
            "**/*\$ViewInjector*.*",
            "**/Dagger*Component.class",
            "**/Dagger*Component\$Builder.class",
            "**/*Module_*Factory.class",
            // We don"t test OpenGL filters.
            "**/com/otaliastudios/cameraview/filters/**.*"
        )
    })
    reports.html.required.set(true)
    reports.xml.required.set(true)
    reports.html.outputLocation.set(file("$coverageOutputDir/html"))
    reports.xml.outputLocation.set(file("$coverageOutputDir/xml/report.xml"))
}