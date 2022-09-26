plugins {
    kotlin("jvm")
}

group = "ru.nstu.isma"
version = "1.0.0"

dependencies {
    implementation(project(":isma-hsm"))
    implementation(project(":isma-lisma"))
    implementation(project(":isma-next-core"))

    implementation (libs.tornadofx.core)
}

val moduleName by extra("isma.isma.next.common.services.main")

tasks {
    compileJava {
        inputs.property("moduleName", moduleName)
        options.compilerArgs = listOf(
            "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
        )
    }
    //check { dependsOn(integTestTask) }
}
