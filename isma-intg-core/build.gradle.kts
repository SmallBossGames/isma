plugins {
    kotlin("jvm")
}

val moduleName by extra("isma.isma.intg.core.main")

dependencies {
    implementation(project(":isma-intg-api"))
    implementation("org.slf4j:slf4j-api:1.7.33")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.jetbrains:annotations:22.0.0")
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