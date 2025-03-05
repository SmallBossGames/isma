plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(project(":isma-compiler:hsm-generator-jvm"))
    implementation(project(":isma-compiler:hsm-core"))
    implementation(project(":isma-compiler:lisma-translator-hsm"))
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-solver:core"))

    implementation ("org.apache.commons:commons-lang3:3.12.0")
    implementation(libs.slf4j.api)
    implementation(libs.com.google.guava)
    implementation(libs.kotlinx.coroutines.core)

    implementation (libs.koin.core)

    testImplementation (libs.junit)
    testImplementation ("com.tngtech.java:junit-dataprovider:1.13.1")
    testImplementation ("com.github.jbellis:jamm:0.3.3")
}
