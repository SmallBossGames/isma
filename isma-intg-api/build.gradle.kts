plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

val moduleName by extra("isma.isma.intg.api.main")

dependencies{
    implementation(libs.kotlinx.coroutines.core)
    implementation("org.jetbrains:annotations:24.0.1")
}
