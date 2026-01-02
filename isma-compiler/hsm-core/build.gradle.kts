plugins {
    alias(libs.plugins.java.modules)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.org.apache.commons.lang)
    implementation(libs.slf4j.api)
    implementation(libs.jetbrains.annotations)

    testImplementation(libs.junit)
}
