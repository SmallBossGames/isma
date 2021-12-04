plugins {
    kotlin("jvm")
}

val moduleName by extra("isma.isma.hsm.main")

dependencies {
    implementation(project(":isma-next-tools"))

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("ch.qos.logback:logback-classic:1.2.7")
    implementation("org.jetbrains:annotations:22.0.0")

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