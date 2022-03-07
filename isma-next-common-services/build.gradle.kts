plugins {
    kotlin("jvm")
}

group = "ru.nstu.isma"
version = "1.0.0"

dependencies {
    implementation(project(":isma-hsm"))
    implementation(project(":isma-lisma"))
    implementation(project(":isma-next-core"))

    implementation ("org.antlr:antlr4-runtime:4.9.3")
}
