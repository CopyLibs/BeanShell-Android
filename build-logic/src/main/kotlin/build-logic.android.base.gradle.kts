@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.BaseExtension

plugins {
    id("com.android.base")
    kotlin("android")
}

extensions.findByType(BaseExtension::class)?.run {
    compileSdkVersion(BuildVersion.COMPILE_SDK)

    defaultConfig {
        minSdk = BuildVersion.MIN_SDK
        targetSdk = BuildVersion.TARGET_SDK
    }

    compileOptions {
        sourceCompatibility = BuildVersion.java
        targetCompatibility = BuildVersion.java
    }
}

kotlin {
    jvmToolchain(BuildVersion.jvmToolchain)
}
