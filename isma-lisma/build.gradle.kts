plugins {
    kotlin("jvm")
}

val moduleName by extra("isma.isma.lisma.main")

dependencies {
    implementation(libs.antlr4.runtime)

    testImplementation("junit:junit:4.13.2")

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
