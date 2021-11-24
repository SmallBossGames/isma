plugins {
    kotlin("jvm")
}

group = "ru.nstu.isma"
version = "1.0.0"

val moduleName by extra("isma.isma.next.tools.main")

dependencies {

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