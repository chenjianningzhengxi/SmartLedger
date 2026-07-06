plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.smartledger.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.smartledger.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

// Debug: print all variant outputs after build
afterEvaluate {
    tasks.matching { it.name.contains("assembleDebug") }.configureEach {
        doLast {
            val outputsDir = file("${project.buildDir}/outputs")
            val apks = fileTree(outputsDir).matching { include("**/*.apk") }
            if (apks.isEmpty()) {
                logger.warn("⚠️ No APK found in $outputsDir")
                fileTree(outputsDir).forEach { f ->
                    logger.warn("   - ${f.relativeTo(project.buildDir)}")
                }
                // Explicitly list all build output directories
                logger.warn("All directories under build/:")
                file(project.buildDir).walkTopDown().maxDepth(4).forEach { d ->
                    if (d.isDirectory) logger.warn("   ${d.relativeTo(project.buildDir)}/")
                }
            } else {
                apks.forEach { logger.lifecycle("✅ APK: ${it.absolutePath}") }
            }
        }
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Coil
    implementation(libs.coil.compose)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
}
