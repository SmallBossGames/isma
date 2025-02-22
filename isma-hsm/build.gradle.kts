plugins {
    alias(libs.plugins.java.modules)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":isma-next-tools"))

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation(libs.jetbrains.annotations)

    testImplementation("junit:junit:4.13.2")
}
