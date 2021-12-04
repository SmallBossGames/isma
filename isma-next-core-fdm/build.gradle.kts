plugins {
    kotlin("jvm")
}

val moduleName by extra("isma.isma.next.core.fdm.main")

group = "ru.nstu.isma"
version = "1.0.0"

dependencies {
    implementation(kotlin("stdlib"))

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