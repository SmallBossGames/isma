plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.java.modules)
    alias(libs.plugins.javafx)

    application
}

application {
    mainModule.set("isma.ui.app.main")
    mainClass.set("ru.isma.next.app.launcher.IsmaApplication")
    applicationDefaultJvmArgs = listOf(
        "--enable-native-access=javafx.graphics",
        "--enable-native-access=io.netty.common",
    )
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs(
        "--enable-native-access=javafx.graphics",
        "--enable-native-access=io.netty.common",
        "-Disma.server.script=$rootDir/build/bundle/isma-server-app/bin/app",
        "-Disma.grin.script=$rootDir/build/bundle/grin-app/bin/app"
    )
}

javafx {
    version = "25.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.koin.core)
    implementation(libs.logback.classic)
    implementation(libs.ikonli.javafx)
    implementation(libs.ikonli.material2.pack)
    implementation(libs.controlsfx)
    implementation(libs.fxmisc.richtext.core)

    implementation(project(":isma-ui:blueprint-editor"))
    implementation(project(":isma-ui:text-editor"))
    implementation(project(":isma-ui:toolkit"))

    implementation(project(":isma-ui:grpc"))
    implementation(project(":isma-ui:external-services"))
    implementation(project(":isma-ui:domain"))
    implementation(libs.grpc.netty)
    implementation(libs.netty.transport)
    implementation(libs.netty.transport.classes.epoll)
    implementation(libs.netty.transport.native.epoll) {
        artifact { classifier = "linux-x86_64" }
    }

    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(project(":isma-ui:toolkit"))
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
    jvmArgs(
        "--add-opens=java.base/java.lang=ALL-UNNAMED",
        "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-opens=java.base/java.util=ALL-UNNAMED",
        "--add-opens=java.base/java.io=ALL-UNNAMED"
    )
    // Run tests on the classpath to avoid JPMS split-package conflicts (e.g. MockK)
    (extensions.getByName("moduleOptions") as org.javamodularity.moduleplugin.extensions.TestModuleOptions).runOnClasspath = true
}
