plugins {
    kotlin("jvm")
    //id("ch.tutteli.gradle.plugins.kotlin.module.info")
}

group = "ru.nstu.isma.next"
version = "1.0.0"

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":isma-hsm"))
    implementation (project(":isma-lisma"))
    implementation (project(":isma-next-tools"))
    implementation (project(":isma-intg-api"))
    implementation (project(":isma-intg-core"))
    implementation (project(":isma-intg-server:isma-intg-server-client"))
    implementation (project(":isma-intg-lib:isma-intg-lib-common"))
    implementation (project(":isma-intg-lib:isma-intg-lib-euler"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1-native-mt")
    testImplementation (project(":isma-intg-lib:isma-intg-lib-common"))

    implementation ("org.apache.commons:commons-lang3:3.12.0")
    implementation ("org.apache.commons:commons-text:1.9")
    implementation ("org.slf4j:slf4j-api:1.7.31")
    implementation ("com.google.guava:guava:30.1.1-jre")

    testImplementation ("junit:junit:4.13.2")
    testImplementation ("com.tngtech.java:junit-dataprovider:1.13.1")
    testImplementation ("com.github.jbellis:jamm:0.3.3")
}