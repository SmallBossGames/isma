plugins {
    alias(libs.plugins.java.modules)

    application
}

application {
    mainModule.set("isma.grin.gui.app")
    mainClass.set("ru.nstu.isma.grin.launcher.LauncherKt")
}

dependencies {
    implementation(libs.koin.core)

    implementation(project(":grin:gui:concatenation"))
    implementation(project(":isma-jvm-lib:exchange-format"))

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}