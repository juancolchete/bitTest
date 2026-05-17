plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.relay.celo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.relay.celo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Android baseline
    implementation("androidx.core:core-ktx:1.12.0")
    
    // EVM execution library for Celo
    implementation("org.web3j:core:4.9.8")
}
