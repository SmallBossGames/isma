plugins {
    kotlin("jvm")
}

group = "ru.nstu.isma"
version = "1.0.0"

val moduleName by extra("isma.isma.next.core.simulation.gen.main")

dependencies {
    implementation(kotlin("stdlib"))

    implementation ("com.google.guava:guava:31.0.1-jre")
    implementation ("org.apache.commons:commons-text:1.9")
    implementation ("org.slf4j:slf4j-api:1.7.32")

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