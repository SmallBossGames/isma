plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-compiler:hsm-core"))
    implementation(project(":isma-next-core"))
    implementation(libs.koin.core)
}
