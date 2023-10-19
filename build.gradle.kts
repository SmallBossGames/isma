import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10" apply false
    id("org.openjfx.javafxplugin") version "0.1.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21" apply false
    id("org.javamodularity.moduleplugin") version "1.8.12" apply false
}

allprojects {
    group = "ru.nstu.isma"
    version = "1.0.0"
}

subprojects {
    apply<JavaPlugin>()

    repositories {
        mavenCentral()
        mavenLocal()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"

        targetCompatibility = "20"
    }
}
