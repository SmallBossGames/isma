plugins {
    id("org.javamodularity.moduleplugin")
}

val moduleName by extra("isma.grin.math.main")

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}

