plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.java.modules) apply false
    alias(libs.plugins.google.protobuf) apply false
    alias(libs.plugins.ben.manes.versions)
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

        targetCompatibility = "25"
    }
}
