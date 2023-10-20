plugins {
     alias(libs.plugins.javafx)
}

subprojects {
    apply(plugin = "org.openjfx.javafxplugin")

    javafx {
        version = "21.0.1"
        modules = listOf("javafx.controls", "javafx.graphics")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

