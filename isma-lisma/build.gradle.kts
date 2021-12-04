plugins {
    kotlin("jvm")
}

val moduleName by extra("isma.isma.lisma.main")

dependencies {
    implementation(project(":isma-hsm"))
    implementation("org.antlr:antlr4-runtime:4.9.3")

    testImplementation("junit:junit:4.13.2")
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
