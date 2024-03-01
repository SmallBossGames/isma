plugins {
    alias(libs.plugins.java.modules)
}

val moduleName by extra("isma.grin.math.main")

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}

