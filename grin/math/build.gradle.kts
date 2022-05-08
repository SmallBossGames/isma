plugins {
    id("de.jjohannes.extra-java-module-info")
}

val moduleName by extra("isma.grin.math.main")

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}


tasks {
    compileJava {
        inputs.property("moduleName", moduleName)
        options.compilerArgs = listOf(
            "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
        )
    }
}

val coroutinesCoreVersion = libs.kotlinx.coroutines.core.get().versionConstraint.requiredVersion

extraJavaModuleInfo {
    automaticModule("kotlinx-coroutines-core-jvm-$coroutinesCoreVersion.jar", "kotlinx.coroutines.core.jvm")
    failOnMissingModuleInfo.set(false)
}


