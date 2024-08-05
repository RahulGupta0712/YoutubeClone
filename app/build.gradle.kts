plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.notyoutube"
    compileSdk = 34

    buildFeatures{
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.notyoutube"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation ("io.github.shashank02051997:FancyToast:2.0.2")  // Fancy Toast
    implementation("com.github.emreesen27:Android-Custom-Toast-Message:1.0.5")  // Custom Toast
    implementation("com.github.f0ris.sweetalert:library:1.5.6") // Sweet ALert Dialog
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation ("com.airbnb.android:lottie:6.4.1")  // Animation JSON
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.28")
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage) // animation gif
    // navigation graph for fragments
    val nav_version = "2.7.7"


    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    implementation ("com.github.Spikeysanju:MotionToast:1.4")   // motion toast

    implementation ("com.github.ibrahimsn98:NiceBottomBar:2.2") // animated bottom navigation bar

    implementation ("com.github.gayanvoice:android-animations-kotlin:1.0.1") // animation on views
    implementation ("com.github.ybq:Android-SpinKit:1.4.0") // loader

    implementation ("nl.joery.timerangepicker:timerangepicker:1.0.0") // time range picker

    // google sign in
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // picasso
    implementation ("com.squareup.picasso:picasso:+")



    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}