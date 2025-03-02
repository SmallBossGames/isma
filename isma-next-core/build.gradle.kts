plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(project(":isma-compiler:hsm-jvm-generator"))
    implementation(project(":isma-compiler:hsm-core"))
    implementation(project(":isma-compiler:lisma-hsm-translator"))
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-intg-core"))
    implementation(project(":isma-intg-lib:isma-intg-lib-euler"))

    implementation ("org.apache.commons:commons-lang3:3.12.0")
    implementation ("org.apache.commons:commons-text:1.10.0")
    implementation ("org.slf4j:slf4j-api:2.0.5")
    implementation(libs.com.google.guava)
    implementation(libs.kotlinx.coroutines.core)

    implementation (libs.koin.core)

    testImplementation (libs.junit)
    testImplementation ("com.tngtech.java:junit-dataprovider:1.13.1")
    testImplementation ("com.github.jbellis:jamm:0.3.3")
}
