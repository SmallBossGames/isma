plugins {
    id("org.openjfx.javafxplugin")
}

subprojects {
    apply(plugin = "org.openjfx.javafxplugin")

    javafx {
        version = "19"
        modules = listOf("javafx.controls", "javafx.graphics")
    }

    tasks.test {
        useJUnitPlatform()
    }
}

