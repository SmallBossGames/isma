plugins {
    alias(libs.plugins.kotlin.jvm)
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "kotlin")

    version = rootProject.version

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
    }

    tasks.test {
        useJUnitPlatform()
    }
}