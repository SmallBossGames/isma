import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
            jvmTarget = "17"
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}