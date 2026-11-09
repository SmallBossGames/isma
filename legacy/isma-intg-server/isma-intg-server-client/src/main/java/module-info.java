module isma.isma.intg.server.isma.intg.server.client.main {
    requires kryonet;
    requires kryo;
    requires com.google.common;
    requires isma.isma.intg.api.main;
    requires isma.isma.intg.server.isma.intg.server.api.main;

    exports ru.nstu.isma.intg.server.client;
}