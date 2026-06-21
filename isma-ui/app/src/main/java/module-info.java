module isma.ui.app.main {
    requires javafx.controls;
    requires javafx.fxml;

    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires kotlinx.coroutines.core;
    requires kotlinx.coroutines.javafx;
    requires kotlinx.serialization.core;
    requires kotlinx.serialization.json;

    requires koin.core.jvm;
    requires org.controlsfx.controls;
    requires org.fxmisc.richtext;
    requires org.slf4j;
    requires org.kordamp.ikonli.javafx;

    requires isma.ui.toolkit;
    requires isma.ui.editor.text;
    requires isma.ui.editor.blueprint;

    requires isma.ui.grpc;
    requires isma.ui.domain;
    requires isma.ui.external.services;
    requires io.grpc.netty;
    requires io.netty.codec;
    requires io.netty.codec.http2;
    requires io.netty.transport;
    requires io.netty.transport.classes.epoll;
    requires io.netty.transport.epoll.linux.x86_64;

    opens ru.isma.next.app.models.preferences to kotlinx.serialization.core;
    opens ru.isma.next.app.models to javafx.base;
    opens ru.isma.next.app.launcher;

    exports ru.isma.next.app.launcher;
}
