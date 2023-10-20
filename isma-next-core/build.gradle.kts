plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

val moduleName by extra("isma.isma.next.core.main")

dependencies {
    implementation(project(":isma-next-core-simulation-gen"))
    implementation(project(":isma-hsm"))
    implementation(project(":isma-lisma"))
    implementation(project(":isma-next-tools"))
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-intg-core"))
    //implementation(project(":isma-intg-server:isma-intg-server-client"))
    implementation(project(":isma-intg-lib:isma-intg-lib-common"))
    implementation(project(":isma-intg-lib:isma-intg-lib-euler"))

    testImplementation (project(":isma-intg-lib:isma-intg-lib-common"))

    implementation ("org.apache.commons:commons-lang3:3.12.0")
    implementation ("org.apache.commons:commons-text:1.10.0")
    implementation ("org.slf4j:slf4j-api:2.0.5")
    implementation ("com.google.guava:guava:31.1-jre")
    implementation(libs.kotlinx.coroutines.core)

    implementation (libs.koin.core)

    testImplementation ("junit:junit:4.13.2")
    testImplementation ("com.tngtech.java:junit-dataprovider:1.13.1")
    testImplementation ("com.github.jbellis:jamm:0.3.3")
}
