plugins {
    kotlin("jvm")
}

group = "ru.nstu.isma"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":isma-next-tools"))
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.jetbrains:annotations:20.1.0")
    implementation(kotlin("stdlib"))
}
