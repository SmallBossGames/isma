plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("org.openjfx.javafxplugin")

    application
}

javafx {
    version = "17.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("ru.isma.next.app.launcher.IsmaApplication")

    /*applicationDefaultJvmArgs = listOf(
        "--add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED",
        "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED"
    )*/
}

dependencies {
    implementation ("no.tornado:tornadofx:${PackageVersion.tornadoFx}")
    implementation ("org.antlr:antlr4-runtime:4.9.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:${PackageVersion.kotlinxCoroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:${PackageVersion.kotlinxCoroutines}")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:${PackageVersion.kotlinReflect}")
    implementation ("io.insert-koin:koin-core:${PackageVersion.koin}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${PackageVersion.kotlinxSerializationJson}")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-material2-pack:12.3.1")
    implementation ("org.fxmisc.richtext:richtextfx:0.10.9")
    implementation("org.controlsfx:controlsfx:11.1.1")

    implementation(project(":isma-hsm"))
    implementation(project(":isma-next-core-fdm"))
    implementation(project(":isma-next-core-simulation-gen"))
    implementation(project(":isma-lisma"))
    implementation(project(":isma-next-core"))
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-next-integration-library"))
    implementation(project(":isma-next-common-services"))
    implementation(project(":isma-blueprint-editor"))
    implementation(project(":isma-text-editor"))
    implementation(project(":isma-next-services-simulation-abstractions"))
    implementation(project(":isma-javafx-extensions"))

    implementation(project(":grin:integration"))

    api(project(":isma-intg-core"))
    api(project(":isma-intg-server:isma-intg-server-client"))
}