plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
    application
}

group = "ru.nstu.isma.server"
version = "1.0.0-SNAPSHOT"

application {
    mainClass.set("ru.nstu.isma.server.app.ApplicationKt")
    mainModule.set("isma.server.app")
}

dependencies {
    // netty-codec-protobuf's module descriptor requires protobuf.javanano,
    // which does not exist for protobuf 4.x; nothing in the runtime uses it.
    configurations.all {
        exclude(group = "io.netty", module = "netty-codec-protobuf")
    }
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
    implementation(libs.logback.classic)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.content.negotiation)
}
