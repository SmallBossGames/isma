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

    implementation("io.grpc:grpc-netty:1.80.0")
    implementation("io.netty:netty-all:4.1.115.Final")
    implementation(libs.grpc.stub)
    implementation(libs.grpc.protobuf)
    implementation(libs.protobuf.java)
    implementation(libs.grpc.java)

    implementation(libs.kotlin.reflect)
    implementation(libs.slf4j.api)
    runtimeOnly("ch.qos.logback:logback-classic:1.5.23")
}
