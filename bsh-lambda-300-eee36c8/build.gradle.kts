import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar

plugins {
    id("build-logic.android.library")
    id("build-logic.bsh.codegen")
    id("build-logic.bsh.checkstyle")
    id("com.vanniktech.maven.publish") version "0.36.0"
    id("signing")
}

val publishGroupId = "io.github.copylibs"
val publishArtifactId = "beanshell-android-lambda"
val publishVersion = "3.0.0.beta11"

android {
    namespace = "bsh.lambda_300_eee36c8"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
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
        groupId = publishGroupId,
        artifactId = publishArtifactId,
        version = publishVersion
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
