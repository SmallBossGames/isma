plugins {
    application
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
}

group = "ru.nstu.isma"
version = "1.0-SNAPSHOT"

val junitVersion = "5.8.1"
val koinVersion = "3.1.3"
val kotlinReflectVersion = "1.6.0"
val kotlinxCoroutinesVersion = "1.5.2-native-mt"
val kotlinxSerializationJsonVersion = "1.3.1"

application {
    mainModule.set("ru.nstu.isma.ismanextpurejfx")
    mainClass.set("ru.nstu.isma.ismanextpurejfx.HelloApplication")
}

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.controlsfx:controlsfx:11.1.0")
    implementation("com.dlsc.formsfx:formsfx-core:11.4.2")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.2.0")
    implementation("org.kordamp.ikonli:ikonli-materialdesign2-pack:12.2.0")

    implementation ("org.antlr:antlr4-runtime:4.9.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:$kotlinReflectVersion")
    implementation ("io.insert-koin:koin-core:$koinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
    implementation ("org.fxmisc.richtext:richtextfx:0.10.7")


    implementation(project(":isma-hsm"))
    implementation(project(":isma-lisma"))
//    implementation(project(":isma-next-core"))
//    implementation(project(":isma-intg-api"))
//    implementation(project(":isma-next-integration-library"))
//    implementation(project(":grin:integration"))
//    implementation(project(":isma-next-common-services"))
    implementation(project(":isma-blueprint-editor"))
    implementation(project(":isma-text-editor"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}