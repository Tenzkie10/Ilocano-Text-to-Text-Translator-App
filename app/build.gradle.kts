plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)  // Ensure Kotlin Compose plugin is added correctly
}

android {
    namespace = "com.example.ilocanotext_to_texttranslator"
    compileSdk = 35  // Use your target SDK version here

    defaultConfig {
        applicationId = "com.example.ilocanotext_to_texttranslator"
        minSdk = 34  // Ensure this matches your app's minimum SDK
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"  // Ensure compatibility with JVM 11
    }

    buildFeatures {
        compose = true  // Enables Compose support
    }
}

dependencies {
    // Core dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Jetpack Compose dependencies
    implementation(platform(libs.androidx.compose.bom))  // BOM for Compose
    implementation(libs.androidx.ui)  // UI components
    implementation(libs.androidx.ui.graphics)  // For graphical elements
    implementation(libs.androidx.ui.tooling.preview)  // Compose tooling for previews
    implementation(libs.androidx.material3)  // Material Design 3 components

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose testing dependencies
    androidTestImplementation(platform(libs.androidx.compose.bom))  // Ensure testing compatibility
    androidTestImplementation(libs.androidx.ui.test.junit4)  // JUnit test support for Compose
    debugImplementation(libs.androidx.ui.tooling)  // Compose tooling for debugging
    debugImplementation(libs.androidx.ui.test.manifest)  // Compose testing manifest
}