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
    implementation(project(":isma-compiler:lisma-translator-hsm"))
    implementation(project(":isma-compiler:hsm-core"))
    implementation(project(":isma-compiler:hsm-fdm"))
    implementation(project(":isma-compiler:hsm-jvm"))

    implementation(libs.slf4j.api)
}
