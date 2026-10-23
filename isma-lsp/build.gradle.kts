plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
    application
}

group = "ru.nstu.isma.lsp"
version = "1.0.0-SNAPSHOT"

application {
    mainClass.set("ru.nstu.isma.lsp.MainKt")
    mainModule.set("isma.lsp")
}

dependencies {
    implementation(project(":isma-compiler:lisma-translator-hsm"))

    implementation(libs.antlr4.runtime)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.slf4j.api)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
    // Run tests on the classpath to avoid JPMS split-package conflicts (e.g. MockK)
    (extensions.getByName("moduleOptions") as org.javamodularity.moduleplugin.extensions.TestModuleOptions).runOnClasspath = true
}
