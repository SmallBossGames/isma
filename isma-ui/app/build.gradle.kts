plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.javafx)
    alias(libs.plugins.java.modules)

    application
}

javafx {
    version = "23.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainModule.set("isma.ui.app")
    mainClass.set("ru.isma.next.app.launcher.IsmaApplication")
}

dependencies {
    implementation(libs.antlr4.runtime)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.core)
    implementation(libs.tornadofx.core)
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-material2-pack:12.3.1")
    implementation("org.controlsfx:controlsfx:11.1.2")

    implementation(project(":isma-compiler:hsm-core"))
    implementation(project(":isma-next-core-fdm"))
    implementation(project(":isma-compiler:hsm-jvm-generator"))
    implementation(project(":isma-compiler:lisma-hsm-translator"))
    implementation(project(":isma-next-core"))
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-next-common-services"))
    implementation(project(":isma-ui:blueprint-editor"))
    implementation(project(":isma-ui:text-editor"))
    implementation(project(":isma-ui:toolkit"))
    implementation(project(":isma-ui:grin-nested"))
    implementation(project(":isma-next-services-simulation-abstractions"))
    implementation(project(":isma-next-integration-library"))


    api(project(":isma-intg-core"))
    //api(project(":isma-intg-server:isma-intg-server-client"))
}
