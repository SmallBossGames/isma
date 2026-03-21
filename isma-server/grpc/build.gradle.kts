import com.google.protobuf.gradle.*

plugins {
    alias(libs.plugins.google.protobuf)
}

group = "ru.nstu.isma.server"
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
            it.plugins {
                id("grpc") {}
            }
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
    implementation("io.grpc:grpc-netty:1.80.0")
    implementation(libs.grpc.stub)
    implementation(libs.grpc.protobuf)
    implementation(libs.protobuf.java)
    implementation(libs.grpc.java)
}
