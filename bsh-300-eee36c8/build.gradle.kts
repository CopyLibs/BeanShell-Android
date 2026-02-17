import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.vanniktech.maven.publish") version "0.36.0"
    id("signing")
}

android {
    namespace = "me.hd.bsh_300_eee36c8"
    compileSdk = 36

    defaultConfig {
        minSdk = 27
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    jvmToolchain(JavaVersion.VERSION_17.toString().toInt())
}

dependencies {
    implementation(libs.dalvik.dx)
}

mavenPublishing {
    configure(
        AndroidSingleVariantLibrary(
            javadocJar = JavadocJar.None(),
            sourcesJar = SourcesJar.Sources(),
            variant = "release",
        )
    )

    coordinates(
        groupId = "io.github.copylibs",
        artifactId = "beanshell-android-lambda",
        version = "3.0.0.beta3"
    )

    pom {
        name = "BeanShell-Android"
        description = "BeanShell Support For Android"
        url = "https://github.com/CopyLibs/BeanShell-Android"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                name = "HdShare"
                url = "https://github.com/HdShare"
            }
        }
        scm {
            url = "https://github.com/CopyLibs/BeanShell-Android"
            connection = "scm:git:git://github.com/CopyLibs/BeanShell-Android.git"
            developerConnection = "scm:git:ssh://git@github.com/CopyLibs/BeanShell-Android.git"
        }
    }

    publishToMavenCentral()
    signAllPublications()
}
