plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    
}

android {
    namespace = "com.example.btl_andnc_quanlydatdoan"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.btl_andnc_quanlydatdoan"
        minSdk = 24
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

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.glide)
    testImplementation(libs.junit)
    implementation(libs.gson)// Check for the latest version
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Firebase
    implementation(libs.firebase.analytics)
    implementation(libs.google.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-storage:21.0.1")

    //Authentication with Credential Manager
    implementation(libs.play.services.auth)
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.googleid.vlatestversion)

}