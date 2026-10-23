plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(project(":isma-compiler:hsm-jvm"))
    implementation(project(":isma-compiler:hsm-jvm-calcmodel"))
    implementation(project(":isma-compiler:hsm-core"))
    implementation(project(":isma-compiler:lisma-translator-hsm"))
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-solver:core"))

    implementation(libs.org.apache.commons.lang)
    implementation(libs.slf4j.api)
    implementation(libs.com.google.guava)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)

    testImplementation(libs.junit)
    testImplementation(libs.junit.dataprovider)
    testImplementation(libs.jamm)
}
