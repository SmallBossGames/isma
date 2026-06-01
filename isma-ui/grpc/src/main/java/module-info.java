module isma.ui.grpc {
    requires io.grpc.netty;
    requires io.grpc.stub;
    requires io.grpc.protobuf;
    requires io.netty.transport;
    requires io.netty.transport.classes.epoll;
    requires io.netty.transport.epoll.linux.x86_64;
    requires com.google.protobuf;
    requires com.google.common;

    exports ru.nstu.isma.contracts.v1.compiler_service;
    exports ru.nstu.isma.contracts.v1.simulation_service;
}
