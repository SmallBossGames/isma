plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(libs.antlr4.runtime)

    testImplementation(libs.junit)

    implementation(project(":isma-compiler:hsm-core"))
}
