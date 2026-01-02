plugins {
    alias(libs.plugins.kotlin.jvm)
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "kotlin")

    version = rootProject.version

    dependencies {
        testImplementation(rootProject.libs.junit.jupiter.engine)
    }

    tasks.test {
        useJUnitPlatform()
    }
}