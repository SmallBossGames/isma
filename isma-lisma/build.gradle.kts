plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(libs.antlr4.runtime)

    testImplementation("junit:junit:4.13.2")

    implementation(project(":isma-hsm"))
}
