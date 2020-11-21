import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    id("org.openjfx.javafxplugin") version "0.0.9"
}
group = "me.smallboss"
version = "1.0-SNAPSHOT"


javafx {
    version = "11.0.2"
    modules("javafx.controls", "javafx.graphics")
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation ("no.tornado:tornadofx:1.7.20")
    implementation(project(":isma-hsm"))
    implementation(project(":isma-lisma"))
    implementation(project(":isma-core"))
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-intg-lib-parent"))
    implementation(project(":isma-intg-lib-parent:isma-intg-lib"))
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}