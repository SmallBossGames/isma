import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "ru.nstu.isma.next"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":isma-hsm"))
    implementation (project(":isma-lisma"))
    implementation (project(":isma-tools"))
    implementation (project(":isma-intg-api"))
    implementation (project(":isma-intg-core"))
    implementation (project(":isma-intg-server:isma-intg-server-client"))
    implementation (project(":isma-intg-lib:isma-intg-lib-common"))
    implementation (project(":isma-intg-lib:isma-intg-lib-euler"))
    testImplementation (project(":isma-intg-lib:isma-intg-lib-common"))

    implementation ("org.apache.commons:commons-lang3:3.11")
    implementation ("org.slf4j:slf4j-api:1.7.7")
    implementation ("com.google.guava:guava:30.0-jre")

    testImplementation ("junit:junit:4.12")
    testImplementation ("com.tngtech.java:junit-dataprovider:1.10.1")
    testImplementation ("com.github.jbellis:jamm:0.3.1")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
