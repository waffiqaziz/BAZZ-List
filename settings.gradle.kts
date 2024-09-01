pluginManagement {
  repositories {
    maven {
      setUrl("https://a8c-libs.s3.amazonaws.com/android")
      content {
        includeGroup("org.wordpress")
        includeGroup("org.wordpress.aztec")
      }
    }
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
    maven { setUrl("https://a8c-libs.s3.amazonaws.com/android") }
  }
}

rootProject.name = "BAZZ List"
include(":app")
 