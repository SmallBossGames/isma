plugins {
    kotlin("jvm") version "1.6.0" apply false
    id("org.openjfx.javafxplugin") version "0.0.10" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.0" apply false
    id("de.jjohannes.extra-java-module-info") version "0.9" apply false
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

        targetCompatibility = "16"
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>{
        targetCompatibility = "16"
    }
}
