plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(libs.org.apache.poi)
    implementation(libs.org.apache.poi.ooxml)
    implementation(libs.de.sciss.jwave)

    implementation(libs.tornadofx.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.koin.core)

    testImplementation (libs.kfixture)
    testImplementation (libs.mockito.kotlin)

    api(project(":grin:gui:common"))
    implementation(project(":grin:math"))
    implementation(project(":isma-ui:toolkit"))
}
