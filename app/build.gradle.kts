plugins {
    id("build-logic.android.application")
}

android {
    namespace = "io.github.copylibs.bsh"

    defaultConfig {
        applicationId = "io.github.copylibs.bsh"
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xno-call-assertions",
            "-Xno-param-assertions",
            "-Xno-receiver-assertions"
        )
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.sweeteditor)
    implementation(project(":bsh-lambda-300-eee36c8"))
}
