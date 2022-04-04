plugins {
    kotlin("jvm") version "1.6.20" apply false
    id("org.openjfx.javafxplugin") version "0.0.12" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.20" apply false
    id("de.jjohannes.extra-java-module-info") version "0.11" apply false
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

        targetCompatibility = "17"
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>{
        targetCompatibility = "17"
        kotlinOptions {
            jvmTarget = "17"
        }
    }
}
