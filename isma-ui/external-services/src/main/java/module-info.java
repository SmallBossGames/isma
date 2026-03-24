module isma.ui.external.services {
    requires kotlin.stdlib;
    requires kotlinx.coroutines.core;
    requires isma.ui.grpc;
    requires isma.ui.domain;
    requires isma.isma.intg.api.main;
    requires io.grpc.netty;
    requires io.grpc.protobuf;
    requires io.grpc.stub;
    requires com.google.protobuf;
    requires io.netty.transport;
    requires io.netty.transport.classes.epoll;
    requires io.netty.transport.epoll.linux.x86_64;
    requires io.netty.transport.unix.common;
    requires io.netty.common;
    requires io.netty.buffer;
    requires io.netty.codec;
    requires com.google.common;
    requires koin.core.jvm;
    requires org.slf4j;
    requires kotlinx.io.core;

    exports ru.isma.next.external;
}
