plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
    application
}

group = "ru.nstu.isma.daemon"
version = "1.0.0-SNAPSHOT"

application {
    mainClass.set("ru.nstu.isma.daemon.app.ApplicationKt")
    mainModule.set("isma.daemon.app")
}

dependencies {
    // netty-codec-protobuf's module descriptor requires protobuf.javanano,
    // which does not exist for protobuf 4.x; nothing in the runtime uses it.
    configurations.all {
        exclude(group = "io.netty", module = "netty-codec-protobuf")
    }
    implementation(project(":isma-daemon:domain"))
    implementation(project(":isma-daemon:grpc"))
    implementation(project(":isma-daemon:infrastructure"))
    // The LISMA compiler CLI is launched as a separate process by the daemon; its
    // classes ship in the app distribution's lib directory. The simulation runner
    // is batteries-included: its full distribution (bin + lib, including the
    // integration-method jars) is embedded separately (see installRunnerDist).
    runtimeOnly(project(":lisma-compiler:cli"))

    implementation(libs.grpc.netty)
    implementation(libs.netty.transport)
    implementation(libs.netty.transport.classes.epoll)
    implementation(libs.netty.transport.native.epoll) {
        artifact {
            classifier = "linux-x86_64"
        }
    }
    implementation(libs.netty.codec)
    implementation(libs.netty.handler)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.protobuf)
    implementation(libs.protobuf.java)
    implementation(libs.grpc.java)
    implementation(libs.grpc.services)

    implementation(libs.kotlin.reflect)
    implementation(libs.koin.core)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.content.negotiation)
}

// Embed the batteries-included simulation-runner distribution (bin + lib, including
// the integration-method jars) into the daemon's distribution so the daemon can
// launch the runner as a self-contained process.
val installRunnerDist by tasks.registering(Copy::class) {
    dependsOn(":isma-solver:cli:installDist")
    from(project(":isma-solver:cli").layout.buildDirectory.dir("install/simulation-runner-cli"))
    into(layout.buildDirectory.dir("install/app/runner"))
}
tasks.named("installDist") {
    // Run AFTER installDist: the application plugin's installDist repopulates the
    // distribution root, so the runner must be copied in afterwards.
    finalizedBy(installRunnerDist)
}
