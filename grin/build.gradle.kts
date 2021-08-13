import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformJvmPlugin

plugins {
    kotlin("jvm")
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "kotlin")

    version = rootProject.version

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "16"
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}