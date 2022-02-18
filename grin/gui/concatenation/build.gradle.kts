plugins {
    kotlin("plugin.serialization")
}

dependencies {
    implementation("org.apache.poi:poi:5.0.0")
    implementation("org.apache.poi:poi-ooxml:5.0.0")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.6.0")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    api(project(":grin:gui:common"))
    implementation(project(":grin:math"))
}
