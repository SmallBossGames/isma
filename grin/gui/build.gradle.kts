plugins {
     alias(libs.plugins.javafx)
}

subprojects {
    apply(plugin = "org.openjfx.javafxplugin")

    javafx {
        version = "25.0.2"
        modules = listOf("javafx.controls", "javafx.graphics")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

