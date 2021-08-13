plugins {
    kotlin("jvm") version "1.5.0" apply false
    id("org.openjfx.javafxplugin") version "0.0.10" apply false
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.0" apply false
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
    }
}
