val moduleName by extra("isma.grin.gui.common.main")

dependencies{
    implementation (project(":grin:analytic-fu"))
}

tasks {
    compileJava {
        inputs.property("moduleName", moduleName)
        options.compilerArgs = listOf(
            "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
        )
    }
}