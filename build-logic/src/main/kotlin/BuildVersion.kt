import org.gradle.api.JavaVersion

object BuildVersion {
    val java = JavaVersion.VERSION_17
    val jvmToolchain = java.toString().toInt()

    const val MIN_SDK = 27
    const val TARGET_SDK = 36
    const val COMPILE_SDK = 36
}
