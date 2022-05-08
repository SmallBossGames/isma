plugins {
    id("org.openjfx.javafxplugin")
}

subprojects {
    apply(plugin = "org.openjfx.javafxplugin")

    javafx {
        version = "17.0.2"
        modules = listOf("javafx.controls", "javafx.graphics")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

