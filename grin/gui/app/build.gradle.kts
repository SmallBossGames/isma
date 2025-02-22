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

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}