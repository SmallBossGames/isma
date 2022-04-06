plugins {
    kotlin("jvm")
    id("de.jjohannes.extra-java-module-info")
}

val moduleName by extra("isma.isma.intg.api.main")

dependencies{
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${PackageVersion.kotlinxCoroutines}")
    implementation("org.jetbrains:annotations:23.0.0")
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
    automaticModule("kotlinx-coroutines-core-jvm-${PackageVersion.kotlinxCoroutines}.jar", "kotlinx.coroutines.core.jvm")
    failOnMissingModuleInfo.set(false)
}