plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "ru.nstu.isma.server"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(project(":isma-server:domain"))
    implementation(project(":isma-jvm-lib:exchange-format"))
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-solver:core"))
    implementation(project(":isma-solver:lib-utils"))
    implementation(project(":isma-compiler:lisma-translator-hsm"))
    implementation(project(":isma-compiler:hsm-core"))
    implementation(project(":isma-compiler:hsm-fdm"))
    implementation(project(":isma-compiler:hsm-jvm"))
    implementation(project(":isma-compiler:hsm-jvm-calcmodel"))
    implementation(project(":isma-next-core"))

    implementation(libs.koin.core)
    implementation(libs.slf4j.api)
}
