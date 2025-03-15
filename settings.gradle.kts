pluginManagement {
    repositories {
        google() // Google Maven Repository
        mavenCentral() // Maven Central Repository
        gradlePluginPortal() // Gradle Plugin Portal
        maven { url = uri("https://jitpack.io") } // JitPack Repository
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "T-VAC-Kotlin"
include(":app")


rootProject.name = "T-VAC-Kotlin"
include(":app")
 