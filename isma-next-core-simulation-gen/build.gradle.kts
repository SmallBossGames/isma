plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "ru.nstu.isma"
version = "1.0.0"

val moduleName by extra("isma.isma.next.core.simulation.gen.main")

dependencies {
    implementation(libs.com.google.guava)
    implementation ("org.apache.commons:commons-text:1.10.0")
    implementation ("org.slf4j:slf4j-api:2.0.5")

    implementation(project(":isma-intg-api"))
    implementation(project(":isma-next-tools"))
    implementation(project(":isma-hsm"))
}

tasks {
    compileJava {
        inputs.property("moduleName", moduleName)
        options.compilerArgs = listOf(
            "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
        )
    }
    //check { dependsOn(integTestTask) }
}