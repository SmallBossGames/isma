plugins {
    kotlin("jvm")
    id("de.jjohannes.extra-java-module-info")
}

val moduleName by extra("isma.isma.intg.api.main")

dependencies{
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
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

extraJavaModuleInfo {
    automaticModule("kotlinx-coroutines-core-jvm-1.6.0-RC.jar", "kotlinx.coroutines.core.jvm")
    failOnMissingModuleInfo.set(false)
}