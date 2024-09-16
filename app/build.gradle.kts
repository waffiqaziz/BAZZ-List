plugins {
  alias(libs.plugins.android.application)   // Android application plugin
  alias(libs.plugins.kotlin.android)        // Kotlin plugin for Android
  id("kotlin-parcelize")                    // Kotlin Parcelize plugin
  alias(libs.plugins.ksp)                   // Kotlin Symbol Processing (KSP) plugin
}

android {
  namespace = "com.waffiq.bazz_list"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.waffiq.bazz_list"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "0.0.1"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    viewBinding = true
  }
  buildTypes {
    debug {
      isDebuggable = true

      // disable below for faster development flow.
//      shrinkResources true
//      minifyEnabled true
//      proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'

      resValue("string", "app_name", "@string/app_name_debug")
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
    }
    release {
      isDebuggable = false
      isShrinkResources = true
      isMinifyEnabled = true
      resValue("string", "app_name", "@string/app_name_release")
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
}

dependencies {
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.core.splashscreen)
  implementation(libs.androidx.appcompat)
  implementation(libs.google.material)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.lifecycle.livedata.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.ktx)
  implementation(libs.androidx.navigation.fragment.ktx)
  implementation(libs.androidx.navigation.ui.ktx)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)

  coreLibraryDesugaring(libs.desugar.jdk.libs)

  // room database
  implementation(libs.androidx.room.runtime)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.legacy.support.v4)
  ksp(libs.androidx.room.room.compiler)

  // Aztec
  implementation(libs.wordpressUtils)
  api(libs.aztec.vv214)

//  // Koin
//  implementation(libs.koin.core)
//  implementation(libs.koin.android)
//  implementation(libs.koin.androidx.navigation)

  // Dagger
  implementation(libs.dagger)
  ksp(libs.dagger.ksp)
}