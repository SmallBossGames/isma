plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies{
    implementation(libs.kotlinx.coroutines.core)
    implementation("org.jetbrains:annotations:24.0.1")
}
