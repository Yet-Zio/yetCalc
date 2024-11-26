// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.aboutLibraries) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}