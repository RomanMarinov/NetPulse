import org.gradle.kotlin.dsl.sourceSets

plugins {
//    alias(libs.plugins.kotlinMultiplatform)
//    alias(libs.plugins.android.kotlin.multiplatform.library)
//    alias(libs.plugins.compose.compiler)
//    alias(libs.plugins.jetbrainsCompose)


    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets

//    androidLibrary {
//        namespace = "app.romanmarinov.sample"
//        compileSdk = 35
//        minSdk = 26
//
////        withHostTestBuilder {  }
////
////        withDeviceTestBuilder {
////            sourceSetTreeName = "test"
////        }.configure {
////            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
////        }
//    }

    androidTarget()

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "sampleKit"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation("androidx.activity:activity-compose:1.9.0")

            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(compose.uiTooling)

        }

        commonMain.dependencies {
            implementation(project(":netpulse"))
//            implementation(libs.kotlin.stdlib)

            implementation(libs.kotlinx.coroutines.core)
////            implementation(libs.compose.runtime)
//            implementation(libs.compose.foundation)
//            implementation(libs.compose.material3)
//            implementation(libs.compose.ui)
////            implementation(libs.compose.resources)


//            implementation(libs.kotlinx.coroutines.core)

            // Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
//            implementation(compose.uiTooling)
            implementation(compose.components.resources)
        }

        iosMain.dependencies {

        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}


android {
    namespace = "app.romanmarinov.sample"

    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
//        sourceSets["main"].resources.srcDirs("src/commonMain/resources")
    sourceSets["main"].resources.srcDirs("src/commonMain/composeResources")

    // ПОКА УСТАНОВЛЕНА МИН 24, А НАДО 26 ПРОВЕРИТЬ libs.versions.android.minSdk.get().toInt()
    defaultConfig {
        applicationId = "app.romanmarinov.sample"
        minSdk = 26
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // удалено
            //isUseProguard = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }

//    dependencies {
//        // ui
//        debugImplementation(compose.uiTooling)
//        implementation(libs.compose.ui)
//        implementation(libs.compose.runtime)
//        implementation(libs.compose.foundation)
//    }
}