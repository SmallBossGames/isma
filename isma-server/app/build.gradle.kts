plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

group = "ru.nstu.isma.server"
version = "1.0.0-SNAPSHOT"

application {
    mainClass.set("ru.nstu.isma.server.app.ApplicationKt")
}

dependencies {
    implementation(project(":isma-server:domain"))
    implementation(project(":isma-server:grpc"))
    implementation(project(":isma-server:infrastructure"))
    implementation(project(":isma-solver:lib-meta"))

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
    runtimeOnly(libs.logback.classic)
}
