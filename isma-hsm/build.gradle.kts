plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":isma-next-tools"))

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("ch.qos.logback:logback-classic:1.2.5")
    implementation("org.jetbrains:annotations:21.0.1")

    testImplementation("junit:junit:4.13.2")
}
