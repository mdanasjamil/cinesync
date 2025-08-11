plugins {

    alias(libs.plugins.android.application)

    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.kotlin.compose)

    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.0"

    alias(libs.plugins.hilt)

    kotlin("kapt")

}



android {

    namespace = "com.example.cinesync"

    compileSdk = 36



    defaultConfig {

        applicationId = "com.example.cinesync"

        minSdk = 24

        targetSdk = 36

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

        jvmTarget = "11"

    }

    buildFeatures {

        compose = true

    }

}



dependencies {



    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)

    implementation(libs.androidx.ui.graphics)

    implementation(libs.androidx.ui.tooling.preview)

    implementation(libs.androidx.material3)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(platform(libs.androidx.compose.bom))

    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)

    debugImplementation(libs.androidx.ui.test.manifest)



// Retrofit

    implementation("com.squareup.retrofit2:retrofit:2.9.0")

// Retrofit with Scalar Converter

    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")



    val nav_version = "2.9.1"

    implementation("androidx.navigation:navigation-compose:$nav_version")

// ViewModel, Coroutines, and StateFlow

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")



// Retrofit for networking

    implementation("com.squareup.retrofit2:retrofit:2.9.0")



// Kotlinx Serialization for parsing JSON

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")



// Coil for loading images from a URL

    implementation("io.coil-kt:coil-compose:2.6.0")



//Dagger-Hilt

    implementation(libs.dagger.hilt.android)

    kapt(libs.dagger.hilt.compiler)

    implementation(kotlin("test"))
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")



// --- TESTING LIBRARIES ---

// Add these lines to fix the "Unresolved reference" errors



// For JUnit, which provides @Test and assertions like assertEquals

    testImplementation("junit:junit:4.13.2")



// For testing coroutines and suspend functions (provides runTest)

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")



// AndroidX testing libraries (for UI tests in the androidTest folder)

    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(platform(libs.androidx.compose.bom))

    androidTestImplementation(libs.androidx.ui.test.junit4)





}