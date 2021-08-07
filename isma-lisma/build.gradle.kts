plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":isma-hsm"))
    implementation("org.antlr:antlr4-runtime:4.9.2")
    testImplementation("junit:junit:4.13.2")
}
