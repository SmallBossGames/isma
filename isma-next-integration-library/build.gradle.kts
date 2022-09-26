plugins {
    kotlin("jvm")
}

group = "ru.nstu.isma.next"
version = "1.0.0"

dependencies {
    implementation(project(":isma-intg-lib"))
    implementation(project(":isma-intg-api"))
    implementation (project(":isma-intg-lib:isma-intg-lib-euler"))
    implementation (project(":isma-intg-lib:isma-intg-lib-rk2"))
    implementation (project(":isma-intg-lib:isma-intg-lib-rk3"))
    implementation (project(":isma-intg-lib:isma-intg-lib-rk31"))
    implementation (project(":isma-intg-lib:isma-intg-lib-rkmerson"))
    implementation (project(":isma-intg-lib:isma-intg-lib-rkfehlberg"))

    implementation("org.slf4j:slf4j-api:1.7.36")
}

val moduleName by extra("isma.isma.next.integration.library.main")


tasks {
    compileJava {
        inputs.property("moduleName", moduleName)
        options.compilerArgs = listOf(
            "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
        )
    }
    //check { dependsOn(integTestTask) }
}