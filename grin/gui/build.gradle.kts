plugins {
    id("org.openjfx.javafxplugin")
}

val kotlinReflectVersion = PackageVersion.kotlinReflect

subprojects {
    apply(plugin = "org.openjfx.javafxplugin")

    javafx {
        version = "17"
        modules = listOf("javafx.controls", "javafx.graphics")
    }

    dependencies {
        implementation (project(":grin:analytic-fu"))
        implementation ("org.jetbrains.kotlin:kotlin-reflect:$kotlinReflectVersion")
        implementation ("no.tornado:tornadofx:${PackageVersion.tornadoFx}")
        implementation ("de.sciss:jwave:1.0.3")
        testImplementation ("ru.kontur.kinfra.kfixture:kfixture:0.6.0")
        testImplementation ("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
