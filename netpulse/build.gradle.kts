import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "io.github.romanmarinov.netpulse"
version = "0.1.2"

kotlin {
//    jvm()

    androidTarget {
        publishLibraryVariants("release")
        withSourcesJar(publish = true)

        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                    freeCompilerArgs.add("-Xjdk-release=${JavaVersion.VERSION_11}")
                }
            }
        }
    }

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

    buildTypes {
        release { }
        debug { }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

mavenPublishing {
    publishToMavenCentral(true)
    signAllPublications() // Обязательно для релизов

    coordinates(
        groupId = "io.github.romanmarinov.netpulse",
        artifactId = "netpulse",
        version = version.toString()
    )

    pom {
        name.set("NetPulse")
        description.set("A Kotlin Multiplatform library for network and performance monitoring.")
        url.set("https://github.com/RomanMarinov/NetPulse")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("romanmarinov")
                name.set("Roman Marinov")
                email.set("marinov37@mail.ru")
            }
        }

        scm {
            url.set("https://github.com/RomanMarinov/NetPulse")
            connection.set("scm:git:git://github.com/RomanMarinov/NetPulse.git")
            developerConnection.set("scm:git:ssh://git@github.com/RomanMarinov/NetPulse.git")
        }
    }
}