plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("org.openjfx.javafxplugin")
    id("de.jjohannes.extra-java-module-info")

    application
}

javafx {
    version = "19"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainModule.set("isma.isma.next.app.main")
    mainClass.set("ru.isma.next.app.launcher.IsmaApplication")

    applicationDefaultJvmArgs = listOf(
        //"--add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED",
        //"--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED"
    )
}

dependencies {
    implementation(libs.antlr4.runtime)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.core)
    implementation(libs.tornadofx.core)
    implementation(libs.fxmisc.richtext.core)
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-material2-pack:12.3.1")
    implementation("org.controlsfx:controlsfx:11.1.2")

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
    //api(project(":isma-intg-server:isma-intg-server-client"))
}

extraJavaModuleInfo {
    failOnMissingModuleInfo.set(false)
}