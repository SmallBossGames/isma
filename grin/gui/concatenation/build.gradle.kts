plugins {
    kotlin("plugin.serialization")
    id("org.javamodularity.moduleplugin")
}

dependencies {
    implementation("org.apache.poi:poi:5.2.2")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("de.sciss:jwave:1.0.3")

    implementation(libs.tornadofx.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.koin.core)

    testImplementation ("ru.kontur.kinfra.kfixture:kfixture:0.6.0")
    testImplementation ("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")

    api(project(":grin:gui:common"))
    implementation(project(":grin:math"))
    implementation(project(":isma-javafx-extensions"))
}
