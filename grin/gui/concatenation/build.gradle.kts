plugins {
    kotlin("plugin.serialization")
}

dependencies {
    implementation("org.apache.poi:poi:5.2.2")
    implementation("org.apache.poi:poi-ooxml:5.2.2")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:${PackageVersion.kotlinxCoroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:${PackageVersion.kotlinxCoroutines}")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${PackageVersion.kotlinxSerializationJson}")

    implementation ("io.insert-koin:koin-core:${PackageVersion.koin}")

    api(project(":grin:gui:common"))
    implementation(project(":grin:math"))
    implementation(project(":isma-javafx-extensions"))
}
