// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

  repositories {
    mavenCentral()
  }
  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
  }
}

plugins {
  alias(libs.plugins.android.application) apply false // Available for app module
  alias(libs.plugins.android.library) apply false // Available for library modules
  alias(libs.plugins.kotlin.android) apply false  // Available for Kotlin in all modules
  alias(libs.plugins.ksp) version libs.versions.ksp.get() apply false // KSP plugin version defined
}