import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    id("maven-publish")


    //     alias(libs.plugins.kotlinMultiplatform)
    //    alias(libs.plugins.androidLibrary) // библиотека, а не приложение
    //    alias(libs.plugins.composeMultiplatform)
    //    alias(libs.plugins.composeCompiler)
    //    id("maven-publish")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
//    androidTarget {
//        compilations.all {
//            compileTaskProvider.configure {
//                compilerOptions {
//                    jvmTarget.set(JvmTarget.JVM_11)
//                }
//            }
//        }
//    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            baseName = "netpulse"
            isStatic = true
            freeCompilerArgs += listOf(
                "-Xbinary=bundleId=app.romanmarinov.netpulse"
            )
        }
    }

    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
//            implementation(compose.runtime)


            api(compose.runtime)
            api(compose.foundation)
            api(compose.material3)
            api(compose.ui)
            api(compose.components.resources)
        }
        iosMain.dependencies {

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "app.romanmarinov.netpulse"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
