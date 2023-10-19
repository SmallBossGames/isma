plugins {
    kotlin("jvm")
    id("org.javamodularity.moduleplugin")
}

val moduleName by extra("isma.isma.intg.api.main")

dependencies{
    implementation(libs.kotlinx.coroutines.core)
    implementation("org.jetbrains:annotations:23.0.0")
}
