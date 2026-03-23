plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

group = "ru.isma.next.ui"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(project(":isma-ui:grpc"))
    implementation(project(":isma-ui:domain"))
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-compiler:hsm-jvm"))
    implementation(project(":isma-compiler:hsm-jvm-calcmodel"))
    implementation(libs.grpc.netty)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.stub)
    implementation(libs.protobuf.java)
    implementation(libs.netty.transport)
    implementation(libs.netty.transport.classes.epoll)
    implementation(libs.netty.transport.native.epoll) {
        artifact { classifier = "linux-x86_64" }
    }
    implementation(libs.netty.codec)
    implementation(libs.com.google.guava)
    implementation(libs.koin.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.slf4j.api)
}
