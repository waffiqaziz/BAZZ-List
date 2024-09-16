plugins {
  alias(libs.plugins.android.application) apply false // Available for app module
  alias(libs.plugins.android.library) apply false // Available for library modules
  alias(libs.plugins.kotlin.android) apply false  // Available for Kotlin in all modules
  alias(libs.plugins.ksp) version libs.versions.ksp.get() apply false // KSP plugin version defined
}