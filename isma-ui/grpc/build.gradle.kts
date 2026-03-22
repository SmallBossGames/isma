import com.google.protobuf.gradle.*

plugins {
    alias(libs.plugins.google.protobuf)
}

group = "ru.isma.next.ui"
version = "1.0.0-SNAPSHOT"

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.protobuf.java.get().version}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.grpc.java.get().version}"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins { id("grpc") {} }
        }
    }
}

sourceSets {
    main {
        proto {
            srcDir("../../protobuf-contracts")
        }
    }
}

dependencies {
    implementation(libs.grpc.netty)
    implementation(libs.netty.transport)
    implementation(libs.netty.transport.classes.epoll)
    implementation(libs.netty.transport.native.epoll) {
        artifact {
            classifier = "linux-x86_64"
        }
    }
    implementation(libs.grpc.stub)
    implementation(libs.grpc.protobuf)
    implementation(libs.protobuf.java)
    implementation(libs.grpc.java)
}
