plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.google.services)
}
android {
    namespace = "com.example.learningai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.learningai"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"


        manifestPlaceholders["GEMINI_API_KEY"] =
            project.findProperty("GEMINI_API_KEY") ?: ""
    }


    buildFeatures {
        compose = true
        buildConfig = true
    }


    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    dependencies {

        /* -------------------- CORE -------------------- */
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.activity:activity-compose:1.9.2")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

        /* -------------------- COMPOSE -------------------- */
        implementation(platform("androidx.compose:compose-bom:2024.06.00"))
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-graphics")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.compose.material3:material3")
        debugImplementation("androidx.compose.ui:ui-tooling")

        /* -------------------- NAVIGATION (STABLE) -------------------- */
        implementation("androidx.navigation:navigation-compose:2.7.7")

        /* -------------------- VIEWMODEL -------------------- */
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

        /* -------------------- FIREBASE -------------------- */
        implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
        implementation("com.google.firebase:firebase-auth-ktx")
        implementation("com.google.firebase:firebase-firestore-ktx")
        implementation("com.google.firebase:firebase-analytics-ktx")

        /* -------------------- ROOM -------------------- */
        implementation("androidx.room:room-runtime:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")
        kapt("androidx.room:room-compiler:2.6.1")

        /* -------------------- NETWORK -------------------- */
        implementation("com.squareup.retrofit2:retrofit:2.11.0")
        implementation("com.squareup.retrofit2:converter-gson:2.11.0")

        /* -------------------- UTILS -------------------- */
        implementation("com.google.code.gson:gson:2.10.1")

        /* -------------------- TEST -------------------- */
        testImplementation("junit:junit:4.13.2")
    }
}