plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

group = "ru.nstu.isma"
version = "1.0.0"

application {
    mainClass.set("ru.nstu.isma.compiler.cli.LismaCompilerCliKt")
    applicationName = "lisma-compiler-cli"
}

dependencies {
    implementation(project(":lisma-compiler:lib"))

    implementation(libs.slf4j.api)
}
